package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.log.pixel.Ansi16ConsolePixelLogger
import eu.iamgio.pikt.log.pixel.Ansi256ConsolePixelLogger
import eu.iamgio.pikt.log.pixel.AsciiBoxConsolePixelLogger
import eu.iamgio.pikt.log.pixel.RGBConsolePixelLogger

/**
 * Triggered by the `--pl[=type]` argument.
 * Defines the logger used to log pixels, for example in case of errors.
 *
 * @see Log.pixelLogger
 * @author Giorgio Garofalo
 */
class PixelLoggerCommand : Command("--pl", isSettingsCommand = true) {

    override fun execute(args: String?) {
        Log.pixelLogger = when(args) {
            "256", null -> Ansi256ConsolePixelLogger()
            "16" -> Ansi16ConsolePixelLogger()
            "rgb" -> RGBConsolePixelLogger()
            "box" -> AsciiBoxConsolePixelLogger()
            "none" -> null
            else -> {
                Log.warn("Pixel logger type '$args' not found. Using '256'.")
                Ansi256ConsolePixelLogger()
            }
        }
    }
}