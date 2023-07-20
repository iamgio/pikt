package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.eval.InjectionData
import eu.iamgio.pikt.properties.PiktProperties
import java.io.*

/**
 * Defines the generic way the content of [evaluator] can be used.
 * When referring to "compilation" within this file, it actually refers to either
 * compilation of an executable file ([Compiler]) or interpretation ([Interpreter]).
 *
 * @param evaluator generated output Kotlin code
 * @param properties Pikt properties containing compilation information
 * @see Compiler
 * @see Interpreter
 * @author Giorgio Garofalo
 */
abstract class AbstractCompiler(protected val evaluator: Evaluator, protected val properties: PiktProperties) {

    /**
     * Output folder.
     */
    protected val outputFolder = File(properties.source.parent, "out")

    /**
     * Temporary source .kt (or .kts if interpreted) file.
     */
    protected abstract val sourceKotlinFile: File

    /**
     * Applies pre-compilation properties to the [evaluator].
     */
    protected abstract fun applyEvaluatorSettings()

    /**
     * A list of compilation targets.
     * @return list of targets
     */
    protected abstract fun getTargets(): List<CompilationTarget>

    /**
     * Task called before the compilation starts.
     * @param target target the code should be compiled for.
     */
    protected abstract fun onPreCompile(target: CompilationTarget)

    /**
     * Generates the command needed by the compiler.
     * @param target target the code should be compiled for
     * @return generated command as an array of arguments to either compile or interpret on [target]
     */
    protected abstract fun generateCommand(target: CompilationTarget): Array<String>

    /**
     * Task called after the compilation finishes.
     * @param target target the code has been compiled for.
     */
    protected abstract fun onPostCompile(target: CompilationTarget)

    /**
     * Reads user input and writes its value to the [stdin].
     * The implementation can choose not to handle input.
     * @param stdin the standard input stream of the process
     */
    protected abstract fun handleInput(stdin: OutputStream)

    /**
     * Prints a line from the process stream.
     * @param line line to be printed
     * @param isError whether the line is from the error stream or not
     */
    protected abstract fun printProcessLine(line: String, isError: Boolean)

    /**
     * Runs the Kotlin compiler
     */
    fun compile() {
        // Create the output folder if absent.
        outputFolder.mkdir()

        // Inject specific variables.
        evaluator.insertInjections(InjectionData.fromProperties(properties))

        // Apply compiler-specific code settings.
        applyEvaluatorSettings()

        // Append the standard library to the output code.
        evaluator.insertImports(properties.libraries)

        // Write the evaluator code to the source file.
        sourceKotlinFile.writeText(evaluator.outputCode)

        // Compile for each target.
        getTargets().forEach { target ->

            // Pre-compilation task.
            onPreCompile(target)

            // Generate and execute the command.
            val command: Array<String> = generateCommand(target)
            val process = ProcessBuilder().command(*command).start()

            // Reads user input if needed.
            // TODO: stop reading lines after the process ends
            // handleInput(process.outputStream) // stdin

            // Print the command output.
            printStream(process.inputStream, isErrorStream = false) // stdout
            printStream(process.errorStream, isErrorStream = true)  // stderr

            // Post-compilation task.
            onPostCompile(target)
        }

        // Delete temporary Kotlin source file.
        sourceKotlinFile.delete()
    }

    /**
     * Reads and prints the content of the process [InputStream].
     */
    private fun printStream(inputStream: InputStream, isErrorStream: Boolean) {
        val reader = BufferedReader(InputStreamReader(inputStream))

        var line: String?

        while(reader.readLine().also { line = it } != null) {
            line?.let {
                if(!it.startsWith(KOTLIN_COMPILER_WARNING) && !it.contains(KOTLIN_COMPILER_XVERIFY)) {
                    printProcessLine(it, isErrorStream)
                }
            }
        }
    }
}