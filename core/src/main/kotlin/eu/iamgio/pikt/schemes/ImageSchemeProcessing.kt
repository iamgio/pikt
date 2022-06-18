package eu.iamgio.pikt.schemes

import eu.iamgio.pikt.image.clone
import eu.iamgio.pikt.image.readLineByLine
import eu.iamgio.pikt.image.rgbToHex
import eu.iamgio.pikt.lib.JarLibrary
import eu.iamgio.pikt.properties.ColorsProperty
import eu.iamgio.pikt.properties.INTERNAL_COLORS_SCHEME_PATH
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*

/**
 * Class that allows operations between an image, its custom scheme and the default internal scheme.
 * @param customScheme custom colors scheme to handle
 * @author Giorgio Garofalo
 */
sealed class ImageSchemeProcessing(protected val image: BufferedImage, private val customScheme: Properties, private val libraries: List<JarLibrary>) {

    /**
     * @return the schemes as a pair of two maps: the internal one and the custom one
     */
    protected fun retrieveSchemes(): SchemesPair {
        fun Properties.asMap() = keys.associate { it.toString() to ColorsProperty.of(getProperty(it.toString())) }
        val internalScheme = Properties().also { it.load(javaClass.getResourceAsStream(INTERNAL_COLORS_SCHEME_PATH)!!) }
        libraries.forEach {
            it.colorScheme?.properties?.asMap()?.forEach { (key, colors) ->
                internalScheme.setProperty(it.info.getFullKey(key), colors.stringify())
            }
        }
        return SchemesPair(internalScheme.asMap(), customScheme.asMap())
    }

    /**
     * Runs the process on a copy of [image].
     */
    abstract fun process(): BufferedImage
}

/**
 * Wrapper of internal and custom color schemes.
 *
 * @param internal internal default scheme
 * @param custom custom scheme
 */
data class SchemesPair(val internal: Map<String, ColorsProperty>, val custom: Map<String, ColorsProperty>)

/**
 * @see eu.iamgio.pikt.command.commands.StandardizeCommand
 */
class StandardizeImageProcessing(image: BufferedImage, customScheme: Properties, libraries: List<JarLibrary>) : ImageSchemeProcessing(image, customScheme, libraries) {

    override fun process(): BufferedImage {
        // Get data.
        val image = super.image.clone()
        val schemes = retrieveSchemes()

        // Read image pixels
        super.image.readLineByLine { x, y ->
            val hex = image.getRGB(x, y).rgbToHex()

            // Check for match between the pixel and a value from the custom scheme.
            schemes.custom.forEach { (key, color) ->
                if(color.has(hex)) {
                    // If found, replace with default color.
                    image.setRGB(x, y, Color.decode("#" + schemes.internal.getValue(key).colors.first()).rgb)
                    return@forEach
                }
            }
        }
        return image
    }
}

/**
 * @see eu.iamgio.pikt.command.commands.RecolorizeCommand
 */
class RecolorizeImageProcessing(image: BufferedImage, customScheme: Properties, libraries: List<JarLibrary>, private val method: ColorChoiceMethod) : ImageSchemeProcessing(image, customScheme, libraries) {

    /**
     * Defines the way colors are picked in case a [ColorsProperty] has more than one.
     * @param get function that picks a color out of a [ColorsProperty]
     */
    enum class ColorChoiceMethod(val get: (ColorsProperty) -> String) {
        /**
         * Picks the first color of the property.
         */
        FIRST({ it.colors.first() }),

        /**
         * Picks the last color of the property.
         */
        LAST({ it.colors.last() }),

        /**
         * Picks a random color from the property.
         */
        RANDOM({ it.colors.random() })
    }

    override fun process(): BufferedImage {
        // Get data.
        val image = super.image
        val schemes = retrieveSchemes()

        // Read image pixels
        super.image.readLineByLine { x, y ->
            val hex = image.getRGB(x, y).rgbToHex()

            // Check for match between the pixel and a value from the internal scheme.
            schemes.internal.forEach { (key, color) ->
                if(color.has(hex)) {
                    // If found, replace with custom color. Its behavior is handled by [method]
                    image.setRGB(x, y, Color.decode("#" + method.get(schemes.custom.getValue(key))).rgb)
                    return@forEach
                }
            }
        }
        return image
    }
}