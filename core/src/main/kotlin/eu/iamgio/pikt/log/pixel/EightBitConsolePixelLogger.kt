package eu.iamgio.pikt.log.pixel

import com.diogonunes.jcolor.Attribute
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.log.console.color.EightBitColorPalette

/**
 * A logger that prints pixels with a 256 (8-bit) color palette, if it is supported by the terminal.
 *
 * @author Giorgio Garofalo
 */
class EightBitConsolePixelLogger : ConsoleColoredPixelLogger() {
    override fun getBackgroundColor(pixel: Pixel): Attribute {
        val colorNumber = EightBitColorPalette.getClosestTo(pixel.color)
        return Attribute.BACK_COLOR(colorNumber)
    }

    override fun getTextColor(pixel: Pixel): Attribute {
        val complementary = pixel.color.complementary
        val colorNumber = EightBitColorPalette.getClosestTo(complementary)
        return Attribute.TEXT_COLOR(colorNumber)
    }
}