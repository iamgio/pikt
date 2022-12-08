package eu.iamgio.pikt.log.pixel

import com.diogonunes.jcolor.Attribute
import eu.iamgio.pikt.image.Color

/**
 * A logger that prints pixels as RGB (256) colors, if they are supported by the terminal.
 *
 * @author Giorgio Garofalo
 */
class RGBConsolePixelLogger : ConsoleColoredPixelLogger() {
    override fun getBackgroundColor(color: Color): Attribute {
        return Attribute.BACK_COLOR(color.red, color.green, color.blue)
    }

    override fun getTextColor(color: Color): Attribute {
        val complementary = color.complementary
        return Attribute.TEXT_COLOR(complementary.red, complementary.green, complementary.blue)
    }
}