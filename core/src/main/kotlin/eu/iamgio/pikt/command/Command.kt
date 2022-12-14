package eu.iamgio.pikt.command

import eu.iamgio.pikt.GlobalSettings

/**
 * Represents a command that can be executed from program arguments.
 *
 * @param name the name of the command
 * @param isSettingsCommand whether this command affects either [GlobalSettings] or any other behavior.
 * @param closeOnComplete whether the program should exit after the commands completed their execution.
 * @see Commands
 * @author Giorgio Garofalo
 */
abstract class Command(val name: String, val isSettingsCommand: Boolean = false, val closeOnComplete: Boolean = false) {

    /**
     * Executes the command.
     * @param args command arguments
     */
    abstract fun execute(args: String? = null)

    override fun toString(): String = "Command[name=$name]"
}