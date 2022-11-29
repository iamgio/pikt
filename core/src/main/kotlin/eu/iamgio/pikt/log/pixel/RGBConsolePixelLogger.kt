package eu.iamgio.pikt.log.pixel

import com.diogonunes.jcolor.Attribute
import eu.iamgio.pikt.image.Color
import eu.iamgio.pikt.image.Pixel

/**
 * A logger that prints pixels as RGB (256) colors, if they are supported by the terminal.
 *
 * @author Giorgio Garofalo
 */
class RGBConsolePixelLogger : ConsoleColoredPixelLogger() {
    override fun getBackgroundColor(pixel: Pixel): Attribute {
        return Attribute.BACK_COLOR(pixel.color.red, pixel.color.green, pixel.color.blue)
    }

    override fun getTextColor(pixel: Pixel): Attribute {
        val color = pixel.color
        val complementary = Color(
            255 - color.red,
            255 - color.green,
            255 - color.blue,
        )
        return Attribute.TEXT_COLOR(complementary.red, complementary.green, complementary.blue)
    }
}