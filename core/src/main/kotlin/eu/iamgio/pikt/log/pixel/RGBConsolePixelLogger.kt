package eu.iamgio.pikt.log.pixel

import eu.iamgio.pikt.image.Color
import eu.iamgio.pikt.log.console.color.ConsoleAttributeColor
import eu.iamgio.pikt.log.console.color.RGBConsoleAttributeColor

/**
 * A logger that prints pixels as RGB (256) colors, if they are supported by the terminal.
 *
 * @author Giorgio Garofalo
 */
class RGBConsolePixelLogger : ConsoleColoredPixelLogger() {
    override fun getBackgroundColor(color: Color): ConsoleAttributeColor {
        return RGBConsoleAttributeColor(color.red, color.green, color.blue)
    }

    override fun getTextColor(color: Color): ConsoleAttributeColor {
        val complementary = color.complementary
        return RGBConsoleAttributeColor(complementary.red, complementary.green, complementary.blue)
    }
}