package eu.iamgio.pikt.log.pixel

import com.diogonunes.jcolor.Ansi
import com.diogonunes.jcolor.Attribute
import java.io.PrintStream

/**
 * A logger that prints pixels to a [stream], usually on a console.
 *
 * @param stream target stream
 * @author Giorgio Garofalo
 */
abstract class ConsolePixelLogger(protected val stream: PrintStream) : PixelLogger {

    /**
     * Logs a string [content] with ANSI [attributes] to [stream]
     */
    protected fun logColorized(content: String, vararg attributes: Attribute) {
        stream.print(Ansi.colorize(content, *attributes))
    }

    override fun newLine() = stream.println()
}

