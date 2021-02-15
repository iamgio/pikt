package eu.iamgio.pikt

import eu.iamgio.pikt.command.Commands
import eu.iamgio.pikt.command.commands.CreateColorsCommand
import eu.iamgio.pikt.compiler.Compiler
import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.statement.Statements
import eu.iamgio.pikt.statement.statements.VariableStatement

fun main(args: Array<String>) {
    val startTime = System.currentTimeMillis()

    registerCommands()
    registerStatements()

    args.forEach {
        val split = Commands.splitCommandLineArgument(it)
        Commands.getCommand(split.first)?.execute(split.second)
    }

    val properties = PiktPropertiesRetriever().retrieve()

    val image = PiktImage(properties.source, properties.colors)
    val evaluator = Evaluator()
    evaluator.evaluate(image)

    println("Output:\n${evaluator.outputCode}\n")

    val compiler = Compiler(evaluator.outputCode, properties)
    compiler.compile()

    val totalTime = (System.currentTimeMillis() - startTime) / 1000.0
    println("Done. (${totalTime}s)")
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