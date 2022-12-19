package eu.iamgio.pikt.log.pixel

import com.diogonunes.jcolor.Ansi
import com.diogonunes.jcolor.Attribute
import eu.iamgio.pikt.image.Color
import eu.iamgio.pikt.log.console.color.ConsoleAttributeColor

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
     * @return a copy of [content] with ANSI [attributes]
     */
    fun colorize(content: String, vararg attributes: Attribute): String {
        return Ansi.colorize(content, *attributes)
    }

    /**
     * Logs a string [content] with ANSI [attributes] to [stream]
     */
    private fun logColorized(content: String, vararg attributes: Attribute) {
        super.print(colorize(content, *attributes))
    }

    override fun logAll(colors: Iterable<Color>, markIndex: Int?) {
        super.newLine()
        colors.forEachIndexed { index, color ->
            logColorized(
                    content = if(index == markIndex) MARKED_CONTENT_STRING else CONTENT_STRING,
                    getBackgroundColor(color).asBackground(),
                    getTextColor(color).asText(),
                    Attribute.BOLD()
            )
        }
        super.newLine()
    }

    /**
     * @return ANSI background attribute for the [color]
     */
    abstract fun getBackgroundColor(color: Color): ConsoleAttributeColor

    /**
     * @return ANSI text color attribute for the mark
     */
    abstract fun getTextColor(color: Color): ConsoleAttributeColor
}

