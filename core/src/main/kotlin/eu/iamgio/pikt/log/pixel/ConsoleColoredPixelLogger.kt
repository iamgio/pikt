package eu.iamgio.pikt.log.pixel

import com.diogonunes.jcolor.Ansi
import com.diogonunes.jcolor.Attribute
import eu.iamgio.pikt.image.Pixel

/**
 * The string to be printed in order to display pixels to console.
 */
private const val CONTENT_STRING = "   "

/**
 * The string to be printed in order to display marked pixels to console.
 */
private const val MARKED_CONTENT_STRING = " ✗ "

/**
 * A logger that prints pixels as colored rectangles, with a mark on some if requested.
 *
 * @author Giorgio Garofalo
 */
abstract class ConsoleColoredPixelLogger : ConsolePixelLogger() {

    override val surroundByEmptyLines: Boolean = true

    /**
     * Logs a string [content] with ANSI [attributes] to [stream]
     */
    private fun logColorized(content: String, vararg attributes: Attribute) {
        stream.print(Ansi.colorize(content, *attributes))
    }

    override fun log(pixel: Pixel, mark: Boolean) {
        logColorized(
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
}

