package eu.iamgio.pikt

import eu.iamgio.pikt.command.Commands
import eu.iamgio.pikt.command.commands.CreateColorsCommand
import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.statement.Statements
import eu.iamgio.pikt.statement.statements.VariableStatement

fun main(args: Array<String>) {
    registerCommands()
    registerStatements()

    args.forEach {
        val split = Commands.splitCommandLineArgument(it)
        Commands.getCommand(split.first)?.execute(split.second)
    }

    val properties = PiktPropertiesRetriever().retrieve()

    val image = PiktImage(properties.source)
    val evaluator = Evaluator()
    evaluator.evaluate(image, properties.colors)

    println("Output:\n" + evaluator.outputCode)
}

/**
 * Registers commands triggered by command-line arguments.
 */
fun registerCommands() = with(Commands) {
    register(CreateColorsCommand())
}

/**
 * Registers code statements.
 */
fun registerStatements() = with(Statements) {
    register(VariableStatement())
}