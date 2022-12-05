package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.image.PixelMask
import eu.iamgio.pikt.image.readImage
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import java.io.File


/**
 * Triggered by -mask=<path> argument.
 *
 * @author Giorgio Garofalo
 */
class MaskCommand : Command("-mask", closeOnComplete = true) {
    override fun execute(args: String?) {
        if(args == null) {
            Log.error("Expected -mask=<mask path>. Exiting.")
            return
        }

        val properties = PiktPropertiesRetriever().retrieve()
        val piktImage = PiktImage(ImageProcessingUtils.read(properties.source), properties.colors)
        val maskFile = File(args)

        if(!maskFile.exists()) {
            Log.error("Mask image $maskFile does not exist.")
            return
        }

        val maskImage = readImage(maskFile)
        val mask = PixelMask.createFrom(maskImage)

        val compacted = piktImage.compacter.compact(maskImage.width, maskImage.height, mask)
        val file = ImageProcessingUtils.save(compacted, properties.source, tag = "masked")

        Log.info("Masked image successfully saved as $file.")
    }
}