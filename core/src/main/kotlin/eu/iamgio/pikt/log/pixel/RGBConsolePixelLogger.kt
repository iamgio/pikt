package eu.iamgio.pikt.log.pixel

import com.diogonunes.jcolor.Ansi
import com.diogonunes.jcolor.Attribute
import eu.iamgio.pikt.image.Pixel
import java.io.PrintStream

/**
 * The string to be printed in order to display pixels to console.
 */
private const val PIXEL_STRING = "   "

/**
 * A logger that prints pixels as RGB (256) colors.
 *
 * @param stream target stream
 * @author Giorgio Garofalo
 */
class RGBConsolePixelLogger(private val stream: PrintStream = System.out) : PixelLogger {
    override fun log(pixel: Pixel) {
        val background = Attribute.BACK_COLOR(pixel.color.red, pixel.color.green, pixel.color.blue)
        stream.print(Ansi.colorize(PIXEL_STRING, background))
    }

    override fun newLine() = stream.println()
}