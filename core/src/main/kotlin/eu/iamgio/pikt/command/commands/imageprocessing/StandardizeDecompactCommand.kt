package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.image.processing.ImageDecompacter
import eu.iamgio.pikt.image.processing.ImageStandardizer
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktPropertiesRetriever

/**
 * Triggered by the `standardecompact` argument.
 *
 * -standardize + -decompact
 *
 * @author Giorgio Garofalo
 */
class StandardizeDecompactCommand : Command("standardecompact", closeOnComplete = true) {
    override fun execute(args: String?) {
        val properties = PiktPropertiesRetriever().retrieve()
        val piktImage = PiktImage(properties)

        val image = ImageDecompacter(piktImage).process()
        val finalImage = ImageStandardizer(image, properties.colors.rawProperties, properties.libraries).process()

        val file = ImageProcessingUtils.save(finalImage, properties.source, tag = "standardecompacted")

        Log.info("Standardized and decompacted image successfully saved as $file.")
    }
}