package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.properties.PiktProperties
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
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

    /**
     * Gets the output file without extension in the target folder.
     * @param target compilation target to get the folder for
     * @return output file
     */
    private fun getOutputFile(target: CompilationTarget) = File(getTargetFolder(target), properties.output)

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

    /**
     * Include libraries into the output JAR file.
     * @param target compilation target
     */
    private fun copyLibraries(target: CompilationTarget) {
        val targetJar = ZipFile(getOutputFile(target).absolutePath + ".jar")
        val stdlibJar = ZipFile(properties.stdlib) // TODO external libraries

        stdlibJar.fileHeaders.filter { !it.fileName.startsWith("META-INF") }.forEach { header ->
            val inputStream = stdlibJar.getInputStream(header)
            targetJar.addStream(inputStream, ZipParameters().apply { fileNameInZip = header.fileName; isIncludeRootFolder = true })
        }
    }

    override fun onPostCompile(target: CompilationTarget) {
        // If the compilation target is the JVM,
        // include libraries into the output JAR file.
        if(target == CompilationTarget.JVM) {
            copyLibraries(target)
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
}