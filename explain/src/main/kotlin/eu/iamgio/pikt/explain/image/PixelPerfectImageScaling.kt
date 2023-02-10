package eu.iamgio.pikt.explain.image

import eu.iamgio.pikt.image.readLineByLine
import java.awt.image.BufferedImage

/**
 * A scaling technique that scales each pixel of the source by a given factor.
 * This lets a pixelated image stay pixelated.
 *
 * @author Giorgio Garofalo
 */
object PixelPerfectImageScaling : ImageScaling {

    /**
     * Executes an action for each position between (0;0) and (maxOffset;maxOffset).
     * @param maxOffset range length
     * @param action (x;y) action to execute
     */
    private fun forEachOffset(maxOffset: Int, action: (Int, Int) -> Unit) {
        (0 until maxOffset).forEach { offsetX ->
            (0 until maxOffset).forEach { offsetY ->
                action(offsetX, offsetY)
            }
        }
    }

    override fun scale(source: BufferedImage, factor: Int): BufferedImage {
        val output = BufferedImage(source.width * factor, source.height * factor, source.type)

        source.readLineByLine { x, y ->
            val rgb = source.getRGB(x, y)
            forEachOffset(factor) { offsetX, offsetY ->
                val targetX = x * factor + offsetX
                val targetY = y * factor + offsetY
                output.setRGB(targetX, targetY, rgb)
            }
        }

        return output
    }
}