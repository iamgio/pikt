package eu.iamgio.pikt.log.pixel

import com.diogonunes.jcolor.Attribute
import eu.iamgio.pikt.image.Pixel
import java.io.PrintStream

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

