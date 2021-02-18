package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.eval.StdLib
import eu.iamgio.pikt.properties.PiktProperties
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader


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
     * Tasked called before the compilation starts.
     * @param target target the code should be compiled for.
     */
    protected abstract fun onPreCompile(target: CompilationTarget)

    /**
     * Generates the command needed by the compiler.
     * @param target target the code should be compiled for
     * @return generated command to either compile or interpret on [target]
     */
    protected abstract fun generateCommand(target: CompilationTarget): String

    /**
     * Tasked called after the compilation finishes.
     * @param target target the code has been compiled for.
     */
    protected abstract fun onPostCompile(target: CompilationTarget)

    /**
     * Runs the Kotlin compiler
     */
    fun compile() {
        // Create the output folder if absent.
        outputFolder.mkdir()

        // Apply compiler-specific code settings.
        applyEvaluatorSettings()

        // Append the standard library to the output code.
        evaluator.appendStdCode(properties.colors.stdlib)

        // Compile for each target.
        getTargets().forEach { target ->

            // Write the evaluator code to the source file.
            sourceKotlinFile.writeText(evaluator.outputCode + "\n" + StdLib.getTargetSpecificFile(target).readContent(null))

            // Pre-compilation task.
            onPreCompile(target)

            // Execute the command.
            val process = Runtime.getRuntime().exec(generateCommand(target))

            // Print the command output.
            printStream(process.inputStream)
            printStream(process.errorStream)

            // Post-compilation task.
            onPostCompile(target)
        }

        // Delete temporary Kotlin source file.
        sourceKotlinFile.delete()
    }

    /**
     * Reads and prints the content of the process [InputStream].
     */
    private fun printStream(inputStream: InputStream) {
        val reader = BufferedReader(InputStreamReader(inputStream))

        var line: String?

        while(reader.readLine().also { line = it } != null) {
            if(line!!.startsWith("WARNING").not()) println(">   $line")
        }
    }
}