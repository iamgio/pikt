package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.image.processing.ImageDecompacter
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktPropertiesRetriever

/**
 * Triggered by -decompact argument.
 *
 * @author Giorgio Garofalo
 */
class DecompactCommand : Command("-decompact", closeOnComplete = true) {
    override fun execute(args: String?) {
        val properties = PiktPropertiesRetriever().retrieve()
        val piktImage = PiktImage(properties)

        val image = ImageDecompacter(piktImage).process()
        val file = ImageProcessingUtils.save(image, properties.source, tag = "decompacted")

        Log.info("Decompacted image successfully saved as $file.")
    }
}