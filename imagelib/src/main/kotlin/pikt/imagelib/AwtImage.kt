package pikt.imagelib

import pikt.error.PiktIOException
import pikt.error.PiktIndexOutOfBoundsException
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

private typealias AWTColor = java.awt.Color

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

    override fun getColor(x: Int, y: Int): Color {
        if (x < 0 || x >= image.width) {
            throw PiktIndexOutOfBoundsException(
                index = x,
                size = image.width,
                reference = object {}
            )
        }

        if (y < 0 || y >= image.height) {
            throw PiktIndexOutOfBoundsException(
                index = y,
                size = image.height,
                reference = object {}
            )
        }

        val rgb = image.getRGB(x, y)
        return AWTColor(rgb).toPiktColor()
    }

    override fun save(file: File) {
        try {
            ImageIO.write(image, file.extension, file)
        } catch (e: IOException) {
            throw PiktIOException(e.message ?: "Could not save image to $file", reference = object {})
        }
    }

    override fun toString() = "AwtImage (width=$width, height=$height)"

    private fun AWTColor.toPiktColor() = Color(red, green, blue)

    companion object : ImageFactory<AwtImage> {

        override fun blank(width: Int, height: Int, transparent: Boolean): AwtImage {
            val type = if (transparent) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB
            return AwtImage(BufferedImage(width, height, type))
        }

        override fun fromFile(file: File): AwtImage {
            try {
                return AwtImage(ImageIO.read(file))
            } catch (e: IOException) {
                throw PiktIOException(e.message ?: "Could not read image from $file", reference = object {})
            }
        }
    }
}