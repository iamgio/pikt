package eu.iamgio.pikt

import eu.iamgio.pikt.command.Commands
import eu.iamgio.pikt.command.commands.CreateColorsCommand
import eu.iamgio.pikt.properties.PiktPropertiesRetriever

fun main(args: Array<String>) {
    registerCommands()

    args.forEach {
        val split = Commands.splitCommandLineArgument(it)
        Commands.getCommand(split.first)?.execute(split.second)
    }

    val properties = PiktPropertiesRetriever().retrieve()
    println(properties)
}

/**
 * Registers commands triggered by command-line arguments
 */
fun registerCommands() = with(Commands) {
    register(CreateColorsCommand())
}