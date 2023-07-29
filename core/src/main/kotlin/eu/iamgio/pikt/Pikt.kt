package eu.iamgio.pikt

import eu.iamgio.pikt.command.CliCommandsUtils
import eu.iamgio.pikt.command.CliCommandsUtils.merge
import eu.iamgio.pikt.command.CliCommandsUtils.parsed
import eu.iamgio.pikt.command.Commands
import eu.iamgio.pikt.command.commands.*
import eu.iamgio.pikt.command.commands.imageprocessing.*
import eu.iamgio.pikt.compiler.Interpreter
import eu.iamgio.pikt.eval.Scope
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.project.PiktProjectInfo
import eu.iamgio.pikt.project.PiktProjectInfoParser
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.statement.StatementFactory
import eu.iamgio.pikt.statement.Statements
import eu.iamgio.pikt.targetlang.KotlinToolFactory
import eu.iamgio.pikt.targetlang.TargetLanguageToolFactory

fun main(args: Array<String>) {
    // Record when Pikt was started.
    val startTime = System.currentTimeMillis()

    // The target language to generate code for.
    // This could be retrieved via properties in the future.
    val toolFactory: TargetLanguageToolFactory = KotlinToolFactory()

    // Register command-line commands and arguments.
    registerCommands()

    // Register code statements.
    registerStatements(toolFactory.statementFactory)

    // Project info is an optional YAML file loaded from -Dproject=path.
    // It may store command-line properties and commands that should be used for a specific project.
    val projectInfo = readProjectInfo()

    // Project task is an optional "small" project info sub-structure loaded from -Dtask=name
    // where 'name' is the name of the task from the project info YAML configuration.
    // Tasks can be used to store executable jobs that should be reused within the same project.
    val projectTask = getProjectInfoTaskFor(projectInfo)

    // Properties saved within project info are saved into System properties,
    // so that PiktPropertiesReceiver can read them as if they were inserted from command line.
    // The importance hierarchy is: (lower number = overrides others)
    // 1) Command line properties (via -Dproperty=...)
    // 2) Project task
    // 3) Project info
    projectTask?.applyProperties()
    projectInfo?.applyProperties()

    // Look for commands/arguments and execute them.
    // If a project info file is used, arguments from it are loaded as well,
    // along with the project task ones, if a task is selected,
    // still following the priority above.
    val commands = CliCommandsUtils.getRawCommands(args)
        .merge(projectInfo?.commands)
        .merge(projectTask?.commands)
        .parsed()
    Commands.executeAll(commands)

    // Retrieve organized properties
    val properties = PiktPropertiesRetriever().retrieve()

    // Initialize the image passing the source file and the custom color scheme.
    val image = PiktImage(properties)

    // Evaluate the image pixel-by-pixel
    // that generates an evaluator, a statement factory and other language-specific implementations.
    val evaluator = toolFactory.newEvaluator()
    evaluator.evaluate(image, Scope.buildMainScope(properties.libraries, properties.colors.libraries))

    // Print Kotlin output if -printoutput is enabled.
    if (CMD_PRINTOUTPUT in GlobalSettings) {
        Log.info("Output:\n${evaluator.outputCode}\n")
    }

    // Stop execution if at least one error has occurred during code generation.
    if (evaluator.isInvalidated) {
        Log.warn("Please fix these issues and try again.")
        return
    }

    // Interpret the generated code if -interpret is enabled.
    if (CMD_INTERPRET in GlobalSettings) {
        val interpreter = Interpreter(evaluator.clone(), properties)
        interpreter.compile()
    }

    // Compile the code output into an executable via the Kotlin compiler.
    // Does not run if -nocompile is enabled.
    if (CMD_NOCOMPILE !in GlobalSettings) {
        val compiler = toolFactory.newCompiler(evaluator.clone(), properties)
        compiler.compile()
    }

    // Print total time elapsed.
    val totalTime = (System.currentTimeMillis() - startTime) / 1000.0
    Log.info("Done. (${totalTime}s)")
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
 * @param projectInfo the project info that contains task specifications
 * @return the selected task to be executed (if exists)
 */
private fun getProjectInfoTaskFor(projectInfo: PiktProjectInfo?): PiktProjectInfo? =
        projectInfo?.let { PiktPropertiesRetriever.getProjectInfoTaskFor(it) }

/**
 * Registers commands triggered by command-line arguments.
 */
fun registerCommands() = with(Commands) {
    register(HelpCommand())
    register(WelcomeCommand())
    register(InterpretCommand())
    register(DownloadCompilerCommand())
    register(CreateSchemeCommand())
    register(ExportSchemeCommand())
    register(PrintOutputCommand())
    register(PixelInfoCommand())
    register(NoCompileCommand())
    register(PixelLoggerCommand())
    register(ImageOutputCommand())
    register(ChainOutputCommand())
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
 * @param factory statement factory to get retrieve the statements from
 */
fun registerStatements(factory: StatementFactory) = with(Statements) {
    register(factory.variableAssignmentStatement())
    register(factory.functionCallStatement())
    register(factory.ifStatement())
    register(factory.elseStatement())
    register(factory.structStatement())
    register(factory.forEachStatement())
    register(factory.whileStatement())
    register(factory.returnStatement())
    register(factory.lambdaOpenStatement())
    register(factory.lambdaCloseStatement())
    register(factory.printStatement())
}