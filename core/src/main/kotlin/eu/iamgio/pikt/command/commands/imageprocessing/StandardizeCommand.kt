package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.processing.ImageStandardizer
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktPropertiesRetriever

/**
 * Triggered by the `standardize` argument.
 *
 * @author Giorgio Garofalo
 */
class StandardizeCommand : Command("standardize", closeOnComplete = true) {
    override fun execute(args: String?) {
        val properties = PiktPropertiesRetriever().retrieve()
        val sourceImage = ImageProcessingUtils.read(properties.source)
        val finalImage = ImageStandardizer(sourceImage, properties.colors.rawProperties, properties.libraries).process()
        val file = ImageProcessingUtils.save(finalImage, properties.source, tag = "standardized")

        Log.info("Standardized image successfully saved as $file.")
    }
}