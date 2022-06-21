package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.*
import eu.iamgio.pikt.lib.JarLibrary
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*

/**
 * Triggered by -standardize argument.
 *
 * @author Giorgio Garofalo
 */
class StandardizeCommand : Command("-standardize", {
    val properties = PiktPropertiesRetriever().retrieve()
    val sourceImage = readImage(properties.source)
    val finalImage = StandardizeImageProcessing(sourceImage, properties.colors.rawProperties, properties.libraries).process()
    val file = ImageProcessingUtils.save(finalImage, properties.source, tag = "standardized")

    println("Standardized image successfully saved as $file.")
}, closeOnComplete = true)

/**
 * Triggered by -standardecompact argument.
 *
 * -standardize + decompact
 *
 * @author Giorgio Garofalo
 */
class StandardizeDecompactCommand : Command("-standardecompact", {
    val properties = PiktPropertiesRetriever().retrieve()
    val piktImage = PiktImage(properties)

    val image = piktImage.compacter.decompact()
    val finalImage = StandardizeImageProcessing(image, properties.colors.rawProperties, properties.libraries).process()

    val file = ImageProcessingUtils.save(finalImage, properties.source, tag = "standardecompacted")

    println("Standardized and decompacted image successfully saved as $file.")
}, closeOnComplete = true)

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