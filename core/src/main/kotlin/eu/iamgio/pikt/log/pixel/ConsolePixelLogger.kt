package eu.iamgio.pikt.log.pixel

import java.io.PrintStream

/**
 * A logger that prints pixels to a [stream], usually on a console.
 *
 * @author Giorgio Garofalo
 */
abstract class ConsolePixelLogger : PixelLogger {

    /**
     * The target stream to print onto.
     */
    var stream: PrintStream = System.out

    override fun newLine() = stream.println()
}

