package eu.iamgio.pikt.command

import eu.iamgio.pikt.util.SUCCESS
import eu.iamgio.pikt.util.exit

/**
 * Command names associated with their optional argument string.
 */
typealias RawCommandsMap = Map<String, String?>

/**
 * Command instances associated with their optional argument string.
 */
typealias CommandsMap = Map<Command?, String?>

/**
 * Single-instance class that contains commands information.
 *
 * @see Command
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
     * Executes commands one by one, settings first.
     * If at least one of them is a 'close on complete' command, the program exits.
     * @param commands commands to be executed
     */
    fun executeAll(commands: CommandsMap) {
        var exit = false
        // Setting commands are executed first.
        commands.entries
            .sortedByDescending { (command, _) -> command?.isSettingsCommand } // Settings come first.
            .forEach { (command, args) ->
            // Execute the command.
            command?.execute(args)
            // If at least one command has a 'close on complete' property, the program exits after the other commands are evaluated.
            if(command?.closeOnComplete == true) exit = true
        }
        if(exit) exit(SUCCESS)
    }
}