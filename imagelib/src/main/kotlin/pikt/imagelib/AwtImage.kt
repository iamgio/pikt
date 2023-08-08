package pikt.imagelib

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * [Image] implementation based on AWT [BufferedImage] for the JVM.
 *
 * @param image AWT image to wrap
 */
class AwtImage(private val image: BufferedImage) : WritableImage {

    override val width: Int
        get() = image.width

    override val height: Int
        get() = image.height

    override fun toString() = "AwtImage (width=$width, height=$height)"

    companion object : ImageFactory<AwtImage> {

        override fun blank(width: Int, height: Int, transparent: Boolean): AwtImage {
            val type = if (transparent) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB
            return AwtImage(BufferedImage(width, height, type))
        }

        override fun fromFile(file: File): AwtImage {
            return AwtImage(ImageIO.read(file))
        }
    }
}