package eu.iamgio.pikt

import eu.iamgio.pikt.command.Commands
import eu.iamgio.pikt.command.commands.*
import eu.iamgio.pikt.compiler.Compiler
import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.statement.Statements
import eu.iamgio.pikt.statement.statements.LambdaCloseStatement
import eu.iamgio.pikt.statement.statements.LambdaOpenStatement
import eu.iamgio.pikt.statement.statements.MethodCallStatement
import eu.iamgio.pikt.statement.statements.VariableStatement

fun main(args: Array<String>) {
    // Record when Pikt was started.
    val startTime = System.currentTimeMillis()

    // Register command-line commands and arguments.
    registerCommands()

    // Register code statements.
    registerStatements()

    // Look for any command/argument.
    args.forEach {
        val split = Commands.splitCommandLineArgument(it)
        Commands.getCommand(split.first)?.execute(split.second)
    }

    // Retrieve organized properties
    val properties = PiktPropertiesRetriever().retrieve()

    // Initialize the image passing the source file and the custom colors scheme.
    val image = PiktImage(properties.source, properties.colors)

    // Evaluate the image pixel-by-pixel
    val evaluator = Evaluator()
    evaluator.evaluate(image)

    // Print Kotlin output if -printoutput is enabled.
    System.getProperty(CMD_PRINTOUTPUT)?.run {
        println("Output:\n${evaluator.outputCode}\n")
    }

    // Compile the code output into an executable via the Kotlin compiler.
    // Does not run if -nocompile is enabled.
    if(System.getProperty(CMD_NOCOMPILE) == null) {
        val compiler = Compiler(evaluator.outputCode, properties)
        compiler.compile()
    }

    // Print total time elapsed.
    val totalTime = (System.currentTimeMillis() - startTime) / 1000.0
    println("Done. (${totalTime}s)")
}

/**
 * Registers commands triggered by command-line arguments.
 */
fun registerCommands() = with(Commands) {
    register(CreateColorsCommand())
    register(PrintOutputCommand())
    register(NoCompileCommand())
}

/**
 * Registers code statements.
 */
fun registerStatements() = with(Statements) {
    register(VariableStatement())
    register(MethodCallStatement())
    register(LambdaOpenStatement())
    register(LambdaCloseStatement())
}