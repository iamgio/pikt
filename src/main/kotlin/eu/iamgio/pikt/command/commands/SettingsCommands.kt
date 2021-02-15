package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command

/**
 * A no-arguments command that enables a given property.
 * @author Giorgio Garofalo
 */
open class SettingsCommand(name: String) : Command(name, { System.setProperty(name, "") })

/**
 * Prints the output code to console
 */
class PrintOutputCommand : SettingsCommand(CMD_PRINTOUTPUT)
const val CMD_PRINTOUTPUT = "-printoutput"

/**
 * Lets Pikt run and generate code without actually compiling it into an executable.
 */
class NoCompileCommand : SettingsCommand(CMD_NOCOMPILE)
const val CMD_NOCOMPILE = "-nocompile"