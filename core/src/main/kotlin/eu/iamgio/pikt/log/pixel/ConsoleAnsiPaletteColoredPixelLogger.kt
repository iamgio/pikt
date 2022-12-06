package eu.iamgio.pikt.log.pixel

import com.diogonunes.jcolor.Attribute
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.log.console.color.EightBitColorPalette
import eu.iamgio.pikt.log.console.color.PaletteColorApproximator

/**
 * An extendable logger that approximates pixel colors to their closest value in an ANSI color palette.
 *
 * @param approximator color approximator for the given palette
 * @author Giorgio Garofalo
 */
open class ConsoleAnsiPaletteColoredPixelLogger(private val approximator: PaletteColorApproximator) : ConsoleColoredPixelLogger() {

    override fun getBackgroundColor(pixel: Pixel): Attribute {
        val colorNumber = approximator.getClosestTo(pixel.color)
        return Attribute.BACK_COLOR(colorNumber)
    }

    override fun getTextColor(pixel: Pixel): Attribute {
        val complementary = pixel.color.complementary
        val colorNumber = approximator.getClosestTo(complementary)
        return Attribute.TEXT_COLOR(colorNumber)
    }
}

/**
 * A logger that prints pixels with a 256 (8-bit) color palette, if it is supported by the terminal.
 *
 * @author Giorgio Garofalo
 */
class EightBitConsolePixelLogger : ConsoleAnsiPaletteColoredPixelLogger(EightBitColorPalette)