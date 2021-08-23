package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.eval.Evaluator
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
        return target.commandGenerator.generateCompileCommand(sourceKotlinFile, File(getTargetFolder(target), properties.output), properties)
    }

    override fun onPostCompile(target: CompilationTarget) {
        // Generate script (.sh, .bat and .command) files
        getStarterScriptFiles(name = properties.output, target).forEach {
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
     * Finds out script types and content based on [target]
     * @param name output name without extension
     * @param target compilation target to find the script for
     * @return array of scripts
     */
    private fun getStarterScriptFiles(name: String, target: CompilationTarget): Array<StarterScriptFile> {
        return when(target) {
            CompilationTarget.JVM -> {
                "java -jar $name.jar".let { command ->
                    arrayOf(
                            StarterScriptFile(StarterScriptFile.Type.SH, command),
                            StarterScriptFile(StarterScriptFile.Type.COMMAND, command),
                            StarterScriptFile(StarterScriptFile.Type.BAT, command)
                    )
                }
            }
            CompilationTarget.NATIVE_WINDOWS -> {
                arrayOf(StarterScriptFile(StarterScriptFile.Type.BAT, "$name.exe"))
            }
            CompilationTarget.NATIVE_LINUX -> {
                arrayOf(StarterScriptFile(StarterScriptFile.Type.SH, "./$name.kexe"))
            }
            CompilationTarget.NATIVE_OSX -> {
                arrayOf(StarterScriptFile(StarterScriptFile.Type.COMMAND, "\"\$(dirname \"\$BASH_SOURCE\")\"/$name.kexe"))
            }
        }
    }
}