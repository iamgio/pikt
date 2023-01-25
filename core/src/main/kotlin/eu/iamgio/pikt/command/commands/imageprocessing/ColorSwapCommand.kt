package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.processing.ColorSwap
import eu.iamgio.pikt.image.processing.ImageColorSwapper
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktPropertiesRetriever

/**
 * Triggered by -colorswap=<swaps> argument,
 * where `<swaps>` is a list of `from:to` separated by a comma.
 *
 * Example: `-swapcolors=FF0000:FFFF00,00FF00:0000FF`
 *
 * @author Giorgio Garofalo
 */
class ColorSwapCommand : Command("-colorswap", closeOnComplete = true) {
    override fun execute(args: String?) {
        if(args == null) {
            Log.error("Usage: -colorswap=<from1:to1,from2,to2,...>. Exiting.")
            return
        }

        val properties = PiktPropertiesRetriever().retrieve()
        val image = ImageProcessingUtils.read(properties.source)

        val finalImage = ImageColorSwapper(image, ColorSwap.parseSwaps(args)).process()

        val file = ImageProcessingUtils.save(finalImage, properties.source, tag = "swapped")

        Log.info("Color-swapped image successfully saved as $file.")
    }
}