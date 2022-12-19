package eu.iamgio.pikt.log.pixel

import eu.iamgio.pikt.image.Color
import eu.iamgio.pikt.log.console.color.*

/**
 * An extendable logger that approximates pixel colors to their closest value in an ANSI color palette.
 *
 * @param approximator color approximator for the given palette
 * @author Giorgio Garofalo
 */
open class ConsoleAnsiPaletteColoredPixelLogger(private val approximator: PaletteColorApproximator) : ConsoleColoredPixelLogger() {

    override fun getBackgroundColor(color: Color): ConsoleAttributeColor {
        val colorNumber = approximator.getClosestTo(color)
        return ColorNumberConsoleAttributeColor(colorNumber)
    }

    override fun getTextColor(color: Color): ConsoleAttributeColor {
        val complementary = color.complementary
        val colorNumber = approximator.getClosestTo(complementary)
        return ColorNumberConsoleAttributeColor(colorNumber)
    }
}

/**
 * A logger that prints pixels with an ANSI 256 (8-bit) color palette, if it is supported by the terminal.
 *
 * @author Giorgio Garofalo
 */
class Ansi256ConsolePixelLogger : ConsoleAnsiPaletteColoredPixelLogger(Ansi256ColorPalette)

/**
 * A logger that prints pixels with an ANSI 16 color palette.
 *
 * @author Giorgio Garofalo
 */
class Ansi16ConsolePixelLogger : ConsoleAnsiPaletteColoredPixelLogger(Ansi16ColorPalette)