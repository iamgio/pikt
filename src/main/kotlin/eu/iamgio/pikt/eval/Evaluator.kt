package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.statement.Statement

/**
 * Evaluates a [PiktImage] in order to generate Kotlin code
 *
 * @param codeBuilder Kotlin code builder
 * @author Giorgio Garofalo
 */
class Evaluator(private val codeBuilder: StringBuilder = StringBuilder()) : Cloneable {

    /**
     * Kotlin code output.
     */
    val outputCode: String
        get() = codeBuilder.toString()

    /**
     * @return a copy of this evaluator containing already generated code
     */
    public override fun clone() = Evaluator(StringBuilder(codeBuilder))

    /**
     * Evaluates [image] source via subdivided pixel readers.
     * @param image pikt image
     * @see outputCode
     */
    fun evaluate(image: PiktImage) {
        val readers = image.reader().subdivide()

        data class QueuedStatement(val statement: Statement, val reader: PixelReader)
        val statements = mutableListOf<QueuedStatement>()

        // Queue statements so that previousStatement and nextStatement can be set.
        readers.forEach { reader ->
            reader.next().let { pixel ->
                pixel?.statement?.let { statements += QueuedStatement(it, reader) }
            }
        }

        statements.forEachIndexed { index, queued ->
            val statement = queued.statement
            val reader = queued.reader

            // Set previous and next statements.
            statement.previousStatement = if(index > 0) statements[index - 1].statement else null
            statement.nextStatement = if(index < statements.size - 1) statements[index + 1].statement else null

            // Generate and append code.
            val code = statement.generate(reader, statement.getSyntax())
            if(reader.isInvalidated) {
                codeBuilder.append("// Output of ${statement.name} was invalidated. See errors for details.\n")
            } else {
                codeBuilder.append(code).append("\n")
            }
        }
    }

    /**
     * Inserts the current [outputCode] into a <tt>fun main()</tt> block.
     */
    fun insertInMain() {
        codeBuilder.insert(0, "fun main() {\n")
        codeBuilder.append("\n}")
    }

    /**
     * Appends the standard library to the output code.
     *
     * @see outputCode
     * @see StdLib
     */
    fun appendStdCode() {
        val stdCode = StdLib.libraryFiles.joinToString(separator = "") { StdLib.LibFile(it).readContent() }
        codeBuilder.insert(0, stdCode)
    }
}