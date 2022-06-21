package eu.iamgio.pikt

import eu.iamgio.pikt.command.Commands
import eu.iamgio.pikt.command.commands.*
import eu.iamgio.pikt.command.commands.imageprocessing.*
import eu.iamgio.pikt.compiler.Compiler
import eu.iamgio.pikt.compiler.Interpreter
import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.PiktProjectInfo
import eu.iamgio.pikt.properties.PiktProjectInfo.Companion.mergeArgs
import eu.iamgio.pikt.properties.PiktProjectInfoParser
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.statement.Statements
import eu.iamgio.pikt.statement.statements.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    // Record when Pikt was started.
    val startTime = System.currentTimeMillis()

    // Register command-line commands and arguments.
    registerCommands()

    // Register code statements.
    registerStatements()

    // Project info is an optional YAML file loaded from -Dproject=path.
    // It may store command-line properties and commands that should be used for a specific project.
    val projectInfo = readProjectInfo()
    // Properties saved within project info are saved into System properties,
    // so that PiktPropertiesReceiver can read them as if they were inserted from command line.
    projectInfo?.applyProperties()

    // Look for any command/argument and execute it.
    // If a project info file is used, arguments from it are loaded as well.
    executeCommands(projectInfo.mergeArgs(args))

    // Retrieve organized properties
    val properties = PiktPropertiesRetriever().retrieve()

    // Initialize the image passing the source file and the custom color scheme.
    val image = PiktImage(properties)

    // Evaluate the image pixel-by-pixel
    val evaluator = Evaluator()
    evaluator.evaluate(image, properties.libraries)

    // Print Kotlin output if -printoutput is enabled.
    if(CMD_PRINTOUTPUT in GlobalSettings) {
        println("Output:\n${evaluator.outputCode}\n")
    }

    // Stop execution if at least one error has occurred during code generation.
    if(evaluator.isInvalidated) {
        println("Please fix these issues and try again.")
        return
    }

    // Interpret the generated code if -interpret is enabled.
    if(CMD_INTERPRET in GlobalSettings) {
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
 * Reads and parses optional project info data.
 * @see PiktProjectInfo
 */
private fun readProjectInfo(): PiktProjectInfo? {
    val projectInfoFile = PiktPropertiesRetriever.getProjectInfoFile() ?: return null
    return PiktProjectInfoParser(projectInfoFile).parse()
}

/**
 * Iterates through the program arguments, finds linked commands and executes them.
 * @param args program arguments
 */
private fun executeCommands(args: Array<String>) {
    var exit = false
    args.map {
        val split = Commands.splitCommandLineArgument(it) // Split raw program arguments. cmd=args -> [cmd, args]; cmd -> [cmd, null]
        Commands.getCommand(split.first) to split.second  // Command linked to the first string paired to its arguments.
    }.sortedByDescending { it.first?.isSettingsCommand }.forEach { (command, args) -> // Sort settings first.
        command?.execute(args)                            // Execute the command.
        if(command?.closeOnComplete == true) exit = true  // If at least one command has a 'close on complete' property,
                                                          // the program exits after the other commands are evaluated.
    }
    if(exit) exitProcess(0)
}

/**
 * Registers commands triggered by command-line arguments.
 */
fun registerCommands() = with(Commands) {
    register(WelcomeCommand())
    register(InterpretCommand())
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
    register(ColorSwapCommand())
    register(MaskCommand())
    register(StringConvertCommand())
}

/**
 * Registers code statements.
 */
fun registerStatements() = with(Statements) {
    register(SetVariableStatement())
    register(FunctionCallStatement())
    register(IfStatement())
    register(ElseStatement())
    register(StructStatement())
    register(ForEachStatement())
    register(LambdaOpenStatement())
    register(LambdaCloseStatement())
    register(PrintStatement())
}