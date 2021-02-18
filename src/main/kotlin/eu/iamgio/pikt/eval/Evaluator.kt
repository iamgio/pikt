package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.PiktImage

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
    public override fun clone() = Evaluator(codeBuilder)

    /**
     * Evaluates [image] source via subdivided pixel readers.
     * @param image pikt image
     * @see outputCode
     */
    fun evaluate(image: PiktImage) {
        val readers = image.reader().subdivide()

        readers.forEach { reader ->
            reader.whileNotNull { pixel ->
                pixel.statement?.generate(reader)?.let { codeBuilder.append(it).append("\n") }
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
     * @param colors standard library color scheme
     * @see outputCode
     */
    fun appendStdCode(colors: Map<String, String>) {
        StdLib.libraryFiles.forEach {
            codeBuilder.append(StdLib.LibFile(it).readContent(colors))
        }
    }
}