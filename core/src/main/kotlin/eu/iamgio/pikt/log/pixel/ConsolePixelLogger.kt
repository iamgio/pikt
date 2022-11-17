package eu.iamgio.pikt.log.pixel

import com.diogonunes.jcolor.Ansi
import com.diogonunes.jcolor.Attribute
import eu.iamgio.pikt.image.Pixel
import java.awt.Color
import java.io.PrintStream

/**
 * A logger that prints pixels to a [stream], usually on a console.
 *
 * @param stream target stream
 * @author Giorgio Garofalo
 */
abstract class ConsolePixelLogger(private val stream: PrintStream) : PixelLogger {

    /**
     * Logs a string [content] with ANSI [attributes] to [stream]
     */
    protected fun logColorized(content: String, vararg attributes: Attribute) {
        stream.print(Ansi.colorize(content, *attributes))
    }

    override fun newLine() = stream.println()
}

/**
 * A logger that prints pixels as colored rectangles, with a mark on some if requested.
 *
 * @param stream target stream
 * @author Giorgio Garofalo
 */
abstract class ConsoleColoredPixelLogger(stream: PrintStream) : ConsolePixelLogger(stream) {
    override fun log(pixel: Pixel, mark: Boolean) {
        super.logColorized(
            content = if(mark) MARKED_CONTENT_STRING else CONTENT_STRING,
            getBackgroundColor(pixel),
            getTextColor(pixel),
            Attribute.BOLD()
        )
    }

    /**
     * @return ANSI background attribute for the [pixel]
     */
    abstract fun getBackgroundColor(pixel: Pixel): Attribute

    /**
     * @return ANSI text color attribute for the mark
     */
    abstract fun getTextColor(pixel: Pixel): Attribute

    companion object {
        /**
         * The string to be printed in order to display pixels to console.
         */
        private const val CONTENT_STRING = "   "

        /**
         * The string to be printed in order to display marked pixels to console.
         */
        private const val MARKED_CONTENT_STRING = " ✗ "
    }
}

/**
 * A logger that prints pixels as RGB (256) colors.
 *
 * @param stream target stream
 * @author Giorgio Garofalo
 */
class RGBConsolePixelLogger(stream: PrintStream = System.out) : ConsoleColoredPixelLogger(stream) {
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