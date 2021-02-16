package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.PiktImage

/**
 * Evaluates a [PiktImage] in order to generate Kotlin code
 *
 * @author Giorgio Garofalo
 */
class Evaluator {

    /**
     * Kotlin code builder.
     */
    private val codeBuilder = StringBuilder()

    /**
     * Kotlin code output.
     */
    val outputCode: String
        get() = codeBuilder.toString()

    /**
     * Evaluates [image] source via subdivided pixel readers
     * @param image pikt image
     */
    fun evaluate(image: PiktImage) {
        val readers = image.reader().subdivide()

        codeBuilder.append("fun main(){\n")
        readers.forEach { reader ->
            reader.whileNotNull { pixel ->
                pixel.statement?.generate(reader)?.let { codeBuilder.append(it).append("\n") }
            }
        }
        codeBuilder.append("}")
    }
}