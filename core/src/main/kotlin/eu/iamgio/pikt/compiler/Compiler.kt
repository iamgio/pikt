package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.eval.StdLib
import eu.iamgio.pikt.properties.PiktProperties
import java.io.File

/**
 * Compiles Kotlin code into an executable file.
 *
 * @param evaluator evaluator containing output code
 * @param properties Pikt properties
 * @author Giorgio Garofalo
 */
class Compiler(evaluator: Evaluator, properties: PiktProperties) : AbstractCompiler(evaluator, properties) {

    override val sourceKotlinFile = File(outputFolder, properties.output + ".kt")

    override fun applyEvaluatorSettings() {
        evaluator.insertInMain()
    }

    override fun getTargets() = properties.compilationTargets

    override fun onPreCompile(target: CompilationTarget) {
        getTargetFolder(target).mkdir()
        println("\nCompiling for target $target. Please wait...\n")
    }

    override fun generateCommand(target: CompilationTarget): Array<String> {
        return target.commandGenerator.generateCompileCommand(sourceKotlinFile, getOutputFile(target), properties)
    }

    override fun onPostCompile(target: CompilationTarget) {
        // If the compilation target is the JVM,
        // include libraries into the output JAR file.
        if(target == CompilationTarget.JVM) {
            StdLib.copyJarLibrary(
                    libraryJarFile = properties.stdlib,
                    targetJarFile = File(getOutputFile(target).absolutePath + ".jar")
            ) // TODO external libraries
        }

        // Generate script (.sh, .bat and .command) files
        target.getStarterScriptFiles(executableName = properties.output).forEach {
            it.create(getTargetFolder(target), name = properties.output)
        }
    }

    override fun printProcessLine(line: String, isError: Boolean) {
        if(isError) {
            System.err.println(line)
        } else {
            println(">\t$line")
        }
    }

    /**
     * Gets the target folder
     * Example: out/windows for target [CompilationTarget.NATIVE_WINDOWS]
     * @param target compilation target
     * @return folder of the target
     */
    private fun getTargetFolder(target: CompilationTarget) = File(outputFolder, target.argName)

    /**
     * Gets the output file without extension in the target folder.
     * @param target compilation target to get the folder for
     * @return output file
     */
    private fun getOutputFile(target: CompilationTarget) = File(getTargetFolder(target), properties.output)
}