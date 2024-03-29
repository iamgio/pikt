package eu.iamgio.pikt.compiler.kotlin

import eu.iamgio.pikt.compiler.CompilationTarget
import eu.iamgio.pikt.compiler.Compiler
import eu.iamgio.pikt.compiler.KOTLIN_COMPILER_ERROR_MESSAGE_HEADER
import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktProperties
import java.io.File
import java.io.IOException

/**
 * Kotlin implementation of [Compiler].
 */
class KotlinCompiler(evaluator: Evaluator, properties: PiktProperties) : Compiler(evaluator, properties) {

    override val sourceFile = File(outputFolder, properties.output + ".kt")

    override val errorMessageHeader = KOTLIN_COMPILER_ERROR_MESSAGE_HEADER

    override fun generateCommand(target: CompilationTarget): Array<String> {
        return KotlinCompilerCommandGenerator(sourceFile, properties).generateCompilationCommand(target, getOutputFile(target))
    }

    override fun onPostCompile(target: CompilationTarget) {
        // If the compilation target is the JVM,
        // include libraries into the output JAR file.
        if (target == CompilationTarget.JVM) {
            val executable = File(getOutputFile(target).absolutePath + ".jar")

            properties.libraries.forEach { library ->
                try {
                    library.applyTo(executable)
                } catch (e: IOException) {
                    Log.error("Could not apply library ${library.name} to $executable: " + e.message)
                }
            }
        }

        super.createStarterScriptFiles(target)
    }
}