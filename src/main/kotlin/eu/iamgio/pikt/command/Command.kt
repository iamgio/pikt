package eu.iamgio.pikt.command

import eu.iamgio.pikt.GlobalSettings
import kotlin.system.exitProcess

/**
 * Represents a command that can be executed from program arguments.
 *
 * @param name the name of the command
 * @param action the task to be run. (args) -> task. Arguments can be `null` if not specified.
 * @param isSettingsCommand whether this command affects either [GlobalSettings] or any other behavior.
 * @param closeOnComplete whether the program should close after the command completed its execution.
 * @author Giorgio Garofalo
 */
open class Command(val name: String, val action: (String?) -> Unit, val isSettingsCommand: Boolean = false, var closeOnComplete: Boolean = false) {

    /**
     * Executes the command.
     * @param arguments command arguments
     */
    fun execute(arguments: String?) {
        action(arguments)
        if(closeOnComplete) exitProcess(0)
    }

    override fun toString(): String = "Command[name=$name]"
}

/**
 * Single-instance class that contains commands information.
 *
 * @author Giorgio Garofalo
 */
object Commands {

    /**
     * List of registered commands.
     */
    private val commands = mutableListOf<Command>()

    /**
     * Registers a new command.
     * @param command command to be registered
     */
    fun register(command: Command) {
        commands += command
    }

    /**
     * Gets a command from name, if exists.
     * @param name command name, case sensitive
     * @return command by name if exists, `null` otherwise.
     */
    fun getCommand(name: String): Command? = commands.firstOrNull { it.name == name }

    /**
     * Splits a command line argument (command=args) into a pair (command, args).
     * If there are no args, the second value of the pair is `null`.
     * @param fullCommand argument from command line
     * @return command splitted into name and arguments, if exist
     */
    fun splitCommandLineArgument(fullCommand: String): Pair<String, String?> {
        return fullCommand.indexOf("=").let { index ->
            if(index != -1) {
                fullCommand.substring(0, index) to fullCommand.substring(index + 1)
            } else {
                fullCommand to null
            }
        }
    }
}