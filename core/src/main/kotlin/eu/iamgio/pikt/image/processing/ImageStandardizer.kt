package eu.iamgio.pikt.image.processing

import eu.iamgio.pikt.image.Color
import eu.iamgio.pikt.image.clone
import eu.iamgio.pikt.image.readLineByLine
import eu.iamgio.pikt.image.rgbToHex
import eu.iamgio.pikt.lib.Libraries
import java.awt.image.BufferedImage
import java.util.*

/**
 * Responsible for translating a source [image] that uses a custom scheme
 * into another that uses the default bundled color scheme.
 *
 * @param image source image data
 * @param customScheme custom color scheme as properties data
 * @param libraries external libraries
 */
class ImageStandardizer(
    image: BufferedImage,
    customScheme: Properties,
    libraries: Libraries
) : ImageSchemeProcessing(image, customScheme, libraries) {

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
                    image.setRGB(x, y, Color.fromHex(schemes.internal.getValue(key).colors.first()).rgb)
                    return@forEach
                }
            }
        }
        return image
    }
}