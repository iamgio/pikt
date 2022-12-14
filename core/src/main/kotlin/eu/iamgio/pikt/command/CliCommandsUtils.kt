package eu.iamgio.pikt.command

import eu.iamgio.pikt.log.Log

/**
 * Utilities for command-line command parsing.
 *
 * @author Giorgio Garofalo
 */
object CliCommandsUtils {

    /**
     * Splits a command line argument (command=args) into a pair (command, args).
     * If there are no args, the second value of the pair is `null`.
     * @param fullCommand argument from command line
     * @return command splitted into name and arguments, if exist
     */
    private fun splitCommandLineArgument(fullCommand: String): Pair<String, String?> {
        return fullCommand.indexOf("=").let { index ->
            if(index != -1) {
                fullCommand.substring(0, index) to fullCommand.substring(index + 1)
            } else {
                fullCommand to null
            }
        }
    }

    /**
     * Converts an array of plain CLI commands (`name=args`) to a `name`-`args` map.
     * @return `name`-`args` command map.
     */
    fun getRawCommands(args: Array<String>): RawCommandsMap {
        return args.associate {
            // Split raw program arguments:
            // cmd=args -> [cmd, args]
            // cmd -> [cmd, null]
            splitCommandLineArgument(it)
        }
    }

    /**
     * Merges two raw commands maps, prioritizing [this] to write over [other].
     * @param other optional map to merge.
     * @return the two maps merged if [other] is not `null`, [this] only otherwise.
     */
    fun RawCommandsMap.merge(other: RawCommandsMap?): RawCommandsMap = when {
        other != null -> other + this
        else -> this
    }

    /**
     * Converts a raw `name`-`args` map to a `command`-`args` map where raw names are linked to [Command] instances.
     * @return parsed command map
     * @see Command.isSettingsCommand
     */
    fun RawCommandsMap.parsed(): CommandsMap {
        return this
            // Command linked to the raw name paired to its arguments.
            .map { (name, args) ->
                val command = Commands.getCommand(name)
                if(command == null) {
                    Log.warn("Unknown command: $name")
                }
                command to args
            }
            .toMap()
    }
}