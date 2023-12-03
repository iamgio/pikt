package pikt.imagelib

import pikt.error.PiktIOException
import pikt.error.PiktIndexOutOfBoundsException
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

private typealias AwtColor = java.awt.Color

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

    private fun checkCoordinates(x: Int, y: Int, reference: Any) {
        if (x < 0 || x >= image.width) {
            throw PiktIndexOutOfBoundsException(
                index = x,
                size = image.width,
                reference
            )
        }

        if (y < 0 || y >= image.height) {
            throw PiktIndexOutOfBoundsException(
                index = y,
                size = image.height,
                reference
            )
        }
    }

    override fun getColor(x: Int, y: Int): Color {
        this.checkCoordinates(x, y, reference = object {})

        val rgb = image.getRGB(x, y)
        return AwtColor(rgb).toPiktColor()
    }

    override fun setColor(x: Int, y: Int, color: Color) {
        this.checkCoordinates(x, y, reference = object {})

        image.setRGB(x, y, color.toAwtColor().rgb)
    }

    override fun save(file: File) {
        try {
            ImageIO.write(image, file.extension, file)
        } catch (e: IOException) {
            throw PiktIOException(e.message ?: "Could not save image to $file", reference = object {})
        }
    }

    override fun toString() = "AwtImage (width=$width, height=$height)"

    private fun AwtColor.toPiktColor() = Color(red, green, blue)
    private fun Color.toAwtColor() = AwtColor(red, green, blue)

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