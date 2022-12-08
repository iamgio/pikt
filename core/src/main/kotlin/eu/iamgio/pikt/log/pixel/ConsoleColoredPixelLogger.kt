package eu.iamgio.pikt.log.pixel

import com.diogonunes.jcolor.Ansi
import com.diogonunes.jcolor.Attribute
import eu.iamgio.pikt.image.Color

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

    /**
     * Logs a string [content] with ANSI [attributes] to [stream]
     */
    private fun logColorized(content: String, vararg attributes: Attribute) {
        super.print(Ansi.colorize(content, *attributes))
    }

    override fun logAll(colors: Iterable<Color>, markIndex: Int?) {
        super.newLine()
        colors.forEachIndexed { index, color ->
            logColorized(
                    content = if(index == markIndex) MARKED_CONTENT_STRING else CONTENT_STRING,
                    getBackgroundColor(color),
                    getTextColor(color),
                    Attribute.BOLD()
            )
        }
        super.newLine()
    }

    /**
     * @return ANSI background attribute for the [color]
     */
    abstract fun getBackgroundColor(color: Color): Attribute

    /**
     * @return ANSI text color attribute for the mark
     */
    abstract fun getTextColor(color: Color): Attribute
}

