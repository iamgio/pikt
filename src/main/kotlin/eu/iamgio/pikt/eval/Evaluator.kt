package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.ColorsProperties

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
     */
    fun evaluate(image: PiktImage, colors: ColorsProperties) {
        val readers = image.reader(colors).subdivide()

        codeBuilder.append("fun main(){")
        readers.forEach { reader ->
            reader.whileNotNull { pixel ->
                pixel.getStatement(colors)?.generate(reader)?.let { codeBuilder.append(it).append(";") }
            }
        }
        codeBuilder.append("}")
    }
}