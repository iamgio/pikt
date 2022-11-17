package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.log.pixel.PixelLogger

/**
 * Triggered by the -pl=type argument.
 * Defines the logger used to log pixels, e.g. in case of errors.
 *
 * @see PixelLogger.currentLogger
 * @author Giorgio Garofalo
 */
class PixelLoggerCommand : Command("-pl", { args ->
    val type = PixelLogger.Type.values().firstOrNull { it.name.equals(args, ignoreCase = true) }

    if(type == null) {
        System.err.println("Pixel logger type $args not found. Available types: "
                + PixelLogger.Type.values().joinToString { it.name.lowercase() })
    } else {
        PixelLogger.currentLogger = type.newLogger()
    }
}, isSettingsCommand = true)