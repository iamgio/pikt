package eu.iamgio.pikt.image.processing

import eu.iamgio.pikt.image.Color
import eu.iamgio.pikt.image.readLineByLine
import eu.iamgio.pikt.image.rgbToHex
import eu.iamgio.pikt.lib.JarLibrary
import eu.iamgio.pikt.properties.ColorsProperty
import java.awt.image.BufferedImage
import java.util.*

/**
 * Responsible for translating a source [image] that uses the default bundled color scheme
 * into another that uses a custom color scheme.
 *
 * @param image source image data
 * @param customScheme custom color scheme as properties data
 * @param libraries external libraries
 */
class ImageRecolorizer(
    image: BufferedImage,
    customScheme: Properties,
    libraries: List<JarLibrary>,
    private val method: ColorChoiceMethod
) : ImageSchemeProcessing(image, customScheme, libraries) {

    /**
     * Defines the way colors are picked in case a [ColorsProperty] has more than one.
     * @param get function that picks a color out of a [ColorsProperty]
     */
    @Suppress("unused")
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
                    image.setRGB(x, y, Color.fromHex(method.get(schemes.custom.getValue(key))).rgb)
                    return@forEach
                }
            }
        }
        return image
    }
}