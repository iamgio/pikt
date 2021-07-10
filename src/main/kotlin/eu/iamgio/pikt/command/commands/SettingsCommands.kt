package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.GlobalSettings
import eu.iamgio.pikt.command.Command

/**
 * A no-arguments command that enables a given property.
 * @author Giorgio Garofalo
 */
open class SettingsCommand(name: String) : Command(name, { GlobalSettings += name })

/**
 * Runs the generated code via the JVM compiler.
 */
class InterpretCommand : SettingsCommand(CMD_INTERPRET)
const val CMD_INTERPRET = "-interpret"

/**
 * Prints the output code to console.
 */
class PrintOutputCommand : SettingsCommand(CMD_PRINTOUTPUT)
const val CMD_PRINTOUTPUT = "-printoutput"

/**
 * Adds pixel coordinates to the output code.
 */
class PixelInfoCommand : SettingsCommand(CMD_PIXELINFO)
const val CMD_PIXELINFO = "-pixelinfo"

/**
 * Lets Pikt run and generate code without actually compiling it into an executable.
 */
class NoCompileCommand : SettingsCommand(CMD_NOCOMPILE)
const val CMD_NOCOMPILE = "-nocompile"