package eu.iamgio.pikt

import eu.iamgio.pikt.command.Commands
import eu.iamgio.pikt.command.commands.*
import eu.iamgio.pikt.compiler.Compiler
import eu.iamgio.pikt.compiler.Interpreter
import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.statement.Statements
import eu.iamgio.pikt.statement.statements.*

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

    // Initialize the image passing the source file and the custom color scheme.
    val image = PiktImage(properties)

    // Evaluate the image pixel-by-pixel
    val evaluator = Evaluator()
    evaluator.evaluate(image)

    // Print Kotlin output if -printoutput is enabled.
    if(CMD_PRINTOUTPUT in GlobalSettings) {
        println("Output:\n${evaluator.outputCode}\n")
    }

    // Stop execution if at least one error has occurred during code generation.
    if(evaluator.isInvalidated) {
        println("Please fix these issues and try again.")
        return
    }

    // Interpret the output code if the "-Dinterpret" property is set.
    if(properties.interpretationTarget != null) {
        val interpreter = Interpreter(evaluator.clone(), properties)
        interpreter.compile()
    }

    // Compile the code output into an executable via the Kotlin compiler.
    // Does not run if -nocompile is enabled.
    if(CMD_NOCOMPILE !in GlobalSettings) {
        val compiler = Compiler(evaluator.clone(), properties)
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
    register(WelcomeCommand())
    register(DownloadCompilerCommand())
    register(CreateSchemeCommand())
    register(ExportSchemeCommand())
    register(PrintOutputCommand())
    register(PixelInfoCommand())
    register(NoCompileCommand())
    register(ImageOutputCommand())
    register(StandardizeCommand())
    register(RecolorizeCommand())
    register(CompactCommand())
    register(DecompactCommand())
    register(StandardizeDecompactCommand())
}

/**
 * Registers code statements.
 */
fun registerStatements() = with(Statements) {
    register(DefineVariableStatement())
    register(MethodCallStatement())
    register(IfStatement())
    register(ElseStatement())
    register(ForEachStatement())
    register(LambdaOpenStatement())
    register(LambdaCloseStatement())
}