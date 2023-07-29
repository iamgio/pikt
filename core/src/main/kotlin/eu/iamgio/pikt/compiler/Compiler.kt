package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktProperties
import java.io.File
import java.io.OutputStream

/**
 * Compiles Kotlin code into an executable file.
 *
 * @param evaluator evaluator containing output code
 * @param properties Pikt properties
 * @author Giorgio Garofalo
 */
abstract class Compiler(evaluator: Evaluator, properties: PiktProperties) : AbstractCompiler(evaluator, properties) {

    private var hasError = false

    abstract val errorMessageHeader: String

    override fun applyEvaluatorSettings() {
        evaluator.insertInMain()
    }

    override fun getTargets() = properties.compilationTargets

    override fun onPreCompile(target: CompilationTarget) {
        getTargetFolder(target).mkdir()
        Log.info("\nCompiling for target $target. Please wait...\n")
    }

    // No input expected during compilation.
    override fun handleInput(stdin: OutputStream) {}

    override fun printProcessLine(line: String, isError: Boolean) {
        if (isError) {
            if (!this.hasError) {
                hasError = true
                Log.error(this.errorMessageHeader)
            }
            Log.error(line)
        } else {
            Log.info(">\t$line")
        }
    }

    /**
     * Gets the target folder
     * Example: out/windows for target [CompilationTarget.NATIVE_WINDOWS]
     * @param target compilation target
     * @return folder of the target
     */
    protected fun getTargetFolder(target: CompilationTarget) = File(outputFolder, target.argName)

    /**
     * Gets the output file without extension in the target folder.
     * @param target compilation target to get the folder for
     * @return output file
     */
    protected fun getOutputFile(target: CompilationTarget) = File(getTargetFolder(target), properties.output)
}