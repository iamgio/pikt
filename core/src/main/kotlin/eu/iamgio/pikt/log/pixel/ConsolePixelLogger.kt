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

    protected fun print(message: Any?) = stream.print(message)
    protected fun println(message: Any?) = stream.println(message)
    protected fun newLine() = stream.println()
}

