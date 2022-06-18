package eu.iamgio.pikt.image

import java.awt.image.BufferedImage

const val NOT_IN_MASK = -1

/**
 * A pixel mask component represents a pixel of a [PixelMask].
 *
 * @param x X coordinate of the pixel
 * @param y Y coordinate of the pixel
 * @author Giorgio Garofalo
 */
data class PixelMaskComponent(val x: Int, val y: Int)

/**
 * A pixel mask represents zones of an image where certain operations should be executed.
 *
 * @param components pixels that compose the mask, where their alpha value is 1-255 (0 = not in mask)
 * @author Giorgio Garofalo
 */
open class PixelMask(private val components: List<PixelMaskComponent>) {
    val size get() = components.size

    open fun getComponentByIndex(index: Int) = components.elementAtOrNull(index)

    companion object {
        fun createFrom(image: BufferedImage): PixelMask {
            val components = mutableListOf<PixelMaskComponent>()
            image.readLineByLine { x, y ->
                val rgb = image.getRGB(x, y)
                val alpha = rgb shr 24 and 0xFF // (rgb >> 24) & 0xFF
                if(alpha == 255) { // Only opqaue pixels are masked
                    components += PixelMaskComponent(x, y)
                }
            }
            return PixelMask(components)
        }
    }
}