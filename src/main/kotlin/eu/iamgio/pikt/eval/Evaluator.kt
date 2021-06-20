package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.image.PixelReader

/**
 * Evaluates a [PiktImage] in order to generate Kotlin code
 *
 * @param codeBuilder Kotlin code builder
 * @param isInvalidated whether code generation has run into an error
 * @author Giorgio Garofalo
 */
class Evaluator(val codeBuilder: StringBuilder = StringBuilder(), isInvalidated: Boolean = false) : Cloneable {

    /**
     * If an evaluator is invalidated, its content will not be compiled.
     * An evaluator gets invalidate whenever the code generation process runs into an error thrown by [PixelReader.error].
     */
    var isInvalidated = isInvalidated
        private set

    /**
     * Kotlin code output.
     */
    val outputCode: String
        get() = codeBuilder.toString()

    /**
     * @return a copy of this evaluator containing already generated code
     */
    public override fun clone() = Evaluator(StringBuilder(codeBuilder), isInvalidated)

    /**
     * Evaluates [image] source via subdivided pixel readers.
     * @param image pikt image
     * @see outputCode
     * @see QueuedStatement.eval
     */
    fun evaluate(image: PiktImage) {
        val readers = image.reader().subdivide()

        val statements = mutableListOf<QueuedStatement>()

        // Queue statements so that previousStatement and nextStatement can be set.
        readers.forEach { reader ->
            reader.next().let { pixel ->
                pixel?.statement?.let { statements += QueuedStatement(it.getEvaluableInstance(), reader) }
            }
        }

        // Evaluate queued statements
        statements.eval(this)
    }

    /**
     * Inserts the current [outputCode] into a `fun main()` block.
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

    /**
     * Invalidates this evaluator.
     */
    fun invalidate() {
        isInvalidated = true
    }
}