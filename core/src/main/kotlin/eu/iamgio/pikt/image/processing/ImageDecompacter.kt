package eu.iamgio.pikt.image.processing

import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.image.applyBackground
import java.awt.image.BufferedImage

/**
 * Responsible for splitting an image into one line per statement,
 * plus possible empty lines depending on spacing strategies.
 *
 * @param piktImage source image data
 * @author Giorgio Garofalo
 */
class ImageDecompacter(
    private val piktImage: PiktImage
) : ImageProcessing {

    /**
     * Creates a copy of the image with one statement per line.
     * @return decompacted copy of the source image
     */
    override fun process(): BufferedImage {
        val lines = ImageDecompacterLinesGenerator().generate(this.piktImage.reader())

        // Create image.
        val image = BufferedImage(lines.maxByOrNull { it.size }!!.size, lines.size, BufferedImage.TYPE_INT_RGB)
        image.applyBackground(piktImage.colors)

        // Append lines to the image.
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, rgb ->
                if(rgb != null) {
                    image.setRGB(x, y, rgb)
                }
            }
        }

        return image
    }
}