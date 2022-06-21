package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.image.PixelMask
import eu.iamgio.pikt.image.readImage
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import java.io.File


/**
 * Triggered by -mask=<path> argument.
 *
 * @author Giorgio Garofalo
 */
class MaskCommand : Command("-mask", { args ->
    if(args == null) {
        System.err.println("Expected -mask=<mask path>. Exiting.")
    } else {
        val properties = PiktPropertiesRetriever().retrieve()
        val piktImage = PiktImage(properties)
        val maskFile = File(args)

        if(!maskFile.exists()) {
            System.err.println("Mask image $maskFile does not exist.")
        } else {
            val maskImage = readImage(maskFile)
            val mask = PixelMask.createFrom(maskImage)

            val compacted = piktImage.compacter.compact(maskImage.width, maskImage.height, mask)
            val file = ImageProcessingUtils.save(compacted, properties.source, tag = "masked")

            println("Masked image successfully saved as $file.")
        }
    }
}, closeOnComplete = true)