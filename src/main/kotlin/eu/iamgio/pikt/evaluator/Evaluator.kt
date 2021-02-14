package eu.iamgio.pikt.evaluator

import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.properties.ColorsProperties

/**
 * Evaluates a [PiktImage] in order to generate Kotlin code
 *
 * @author Giorgio Garofalo
 */
class Evaluator {

    /**
     * Kotlin code builder
     */
    private val codeBuilder = StringBuilder()

    /**
     * Kotlin code output
     */
    val outputCode: String
        get() = codeBuilder.toString()

    /**
     * Evaluates [image] source via subdivided pixel readers
     */
    fun evaluate(image: PiktImage, colors: ColorsProperties) {
        val readers = image.reader().subdivide(colors)

        readers.forEach { reader ->
            var pixel: Pixel? = null
            while(reader.next()?.also { pixel = it } != null) {
                pixel!!.getStatement(colors)?.generate(reader)?.let { codeBuilder.append(it).append(";") }
            }
        }
    }
}