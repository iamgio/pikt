package eu.iamgio.pikt.command

/**
 * Represents a command that can be executed from program arguments.
 *
 * @param name the name of the command
 * @param action the task to be run. (args) -> task. Arguments can be <tt>null</tt> if not specified.
 * @author Giorgio Garofalo
 */
open class Command(val name: String, val action: (String?) -> Unit) {

    /**
     * Executes the command.
     * @param arguments command arguments
     */
    fun execute(arguments: String?) = action(arguments)

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
     * @return command by name if exists, <tt>null</tt> otherwise.
     */
    fun getCommand(name: String): Command? = commands.firstOrNull { it.name == name }

    /**
     * Splits a command line argument (command=args) into a pair (command, args).
     * If there are no args, the second value of the pair is <tt>null</tt>.
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