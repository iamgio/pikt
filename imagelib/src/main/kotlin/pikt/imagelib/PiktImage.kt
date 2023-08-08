package pikt.imagelib

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * [Image] implementation for JVM [BufferedImage]s.
 *
 * @param image image to wrap
 */
class PiktImage(private val image: BufferedImage) : WritableImage {

    override val width: Int
        get() = image.width

    override val height: Int
        get() = image.height

    override fun toString() = "PiktImage (width=$width, height=$height)"

    companion object {

        /**
         * Instantiates a blank image of the given size.
         * @param width image width
         * @param height image height
         * @param imageType type of the image to create
         */
        fun blank(width: Int, height: Int, imageType: Int = BufferedImage.TYPE_INT_RGB): PiktImage {
            return PiktImage(BufferedImage(width, height, imageType))
        }

        /**
         * Loads an image from file.
         * @param file file to load the image from
         * @return the loaded image
         * @throws IOException if the image could not be loaded
         */
        fun fromFile(file: File): PiktImage {
            return PiktImage(ImageIO.read(file))
        }
    }
}