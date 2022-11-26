package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.Color
import eu.iamgio.pikt.image.readLineByLine
import eu.iamgio.pikt.image.rgbToHex
import eu.iamgio.pikt.lib.JarLibrary
import eu.iamgio.pikt.logger.Log
import eu.iamgio.pikt.properties.ColorsProperty
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import java.awt.image.BufferedImage
import java.util.*

/**
 * Triggered by -recolorize[=method] argument.
 *
 * @author Giorgio Garofalo
 */
class RecolorizeCommand : Command("-recolorize", closeOnComplete = true) {
    override fun execute(args: String?) {
        val properties = PiktPropertiesRetriever().retrieve()
        val sourceImage = ImageProcessingUtils.read(properties.source)

        // Get color choice method from optional =method argument. Defaults to FIRST.
        val method = if(!args.isNullOrEmpty()) {
            RecolorizeImageProcessing.ColorChoiceMethod.values().firstOrNull { it.name.equals(args, ignoreCase = true) }
                ?: RecolorizeImageProcessing.ColorChoiceMethod.FIRST.also {
                    Log.warn(
                        "Color choice method $args not found. Available methods: ${
                            RecolorizeImageProcessing.ColorChoiceMethod.values().joinToString { it.name.lowercase() }
                        }. Using 'first'."
                    )
                }
        } else {
            RecolorizeImageProcessing.ColorChoiceMethod.FIRST
        }

        val finalImage = RecolorizeImageProcessing(
            sourceImage,
            properties.colors.rawProperties,
            properties.libraries,
            method
        ).process()

        val file = ImageProcessingUtils.save(finalImage, properties.source, tag = "recolorized")

        Log.info("Recolorized image successfully saved as $file.")
    }
}

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
                    image.setRGB(x, y, Color.fromHex(method.get(schemes.custom.getValue(key))).rgb)
                    return@forEach
                }
            }
        }
        return image
    }
}