package eu.iamgio.pikt.image.processing

import eu.iamgio.pikt.image.Color
import eu.iamgio.pikt.image.readLineByLine
import eu.iamgio.pikt.image.rgbToHex
import eu.iamgio.pikt.log.Log
import java.awt.image.BufferedImage

/**
 * Represents a color swap from an image.
 * @param fromHex hexadecimal color value to be replaced by [toHex]
 * @param toHex hexadecimal color value to replace [fromHex]
 */
data class ColorSwap(val fromHex: String, val toHex: String) {
    companion object {

        private const val LIST_SEPARATOR = ","
        private const val PARTS_SEPARATOR = ":"

        /**
         * Parses a list of [ColorSwap]s from a raw string.
         * The string is defined as:
         * ```
         * from1:to1,from2:to2,...
         * ```
         */
        fun parseSwaps(raw: String): List<ColorSwap> {
            return raw.split(LIST_SEPARATOR).asSequence()
                .map { it.split(PARTS_SEPARATOR) }
                .filter {
                    (it.size == 2).also { valid ->
                        if(!valid) {
                            Log.warn("Invalid color swap: $it")
                        }
                    }
                }
                .map {
                    val (from, to) = it
                    ColorSwap(from.uppercase(), to.uppercase())
                }
                .toList()
        }
    }
}

/**
 * Responsible for swapping arbitrary colors within an [image].
 *
 * @param image source and target image data
 * @param swaps list of from-to color pairs
 */
class ImageColorSwapper(private val image: BufferedImage, private val swaps: List<ColorSwap>) : ImageProcessing {
    override fun process(): BufferedImage {
        image.readLineByLine { x, y ->
            val hex = image.getRGB(x, y).rgbToHex()
            val swap = swaps.firstOrNull { it.fromHex == hex }
            if(swap != null) {
                image.setRGB(x, y, Color.fromHex(swap.toHex).rgb)
            }
        }
        return image
    }
}