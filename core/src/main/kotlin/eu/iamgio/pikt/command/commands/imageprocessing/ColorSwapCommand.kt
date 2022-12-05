package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.Color
import eu.iamgio.pikt.image.readLineByLine
import eu.iamgio.pikt.image.rgbToHex
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import java.awt.image.BufferedImage

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

        val finalImage = ColorSwapProcessing(image, ColorSwap.parseSwaps(args)).process()

        val file = ImageProcessingUtils.save(finalImage, properties.source, tag = "swapped")

        Log.info("Color-swapped image successfully saved as $file.")
    }
}

/**
 * Represents a color swap from an image.
 * @param fromHex hexadecimal color value to be replaced by [toHex]
 * @param toHex hexadecimal color value to replace [fromHex]
 */
data class ColorSwap(val fromHex: String, val toHex: String) {
    companion object {
        /**
         * Parses a list of [ColorSwap]s from a raw string.
         * The string is defined as:
         * ```
         * from1:to1,from2:to2,...
         * ```
         */
        fun parseSwaps(raw: String): List<ColorSwap> {
            return raw.split(",").map {
                val (from, to) = it.split(":")
                ColorSwap(from.uppercase(), to.uppercase())
            }
        }
    }
}

class ColorSwapProcessing(private val image: BufferedImage, private val swaps: List<ColorSwap>) : ImageProcessing {
    override fun process(): BufferedImage {
        image.readLineByLine { x, y ->
            val hex = image.getRGB(x, y).rgbToHex()
            val swap = swaps.firstOrNull { it.fromHex == hex }
            if(swap != null) image.setRGB(x, y, Color.fromHex(swap.toHex).rgb)
        }
        return image
    }
}