package eu.iamgio.pikt.compiler.kotlin

import eu.iamgio.pikt.compiler.CompilationTarget
import eu.iamgio.pikt.compiler.CompilerCommandGenerator
import eu.iamgio.pikt.compiler.UnsupportedCompilationTargetException
import eu.iamgio.pikt.properties.PiktProperties
import java.io.File

/**
 * Kotlin implementation of [CompilerCommandGenerator].
 *
 * @param sourceFile temporary Kotlin source file
 * @param properties Pikt properties
 */
class KotlinCompilerCommandGenerator(
    private val sourceFile: File,
    private val properties: PiktProperties
) : CompilerCommandGenerator {

    private fun generateJvmCompilationCommand(outputFile: File) = arrayOf(
        properties.jvmCompilerPath!!,
        sourceFile.absolutePath,
        "-nowarn", "-include-runtime", "-no-reflect",
        "-cp", properties.libraries.getPath(),
        "-d", "$outputFile.jar"
    )

    private fun generateJvmInterpretationCommand() = arrayOf(
        properties.jvmCompilerPath!!,
        "-script", sourceFile.absolutePath,
        "-cp", properties.libraries.getPath()
    )

    /**
     * Generates the command needed by native compilers.
     * @param platform target platform name
     * @param outputFile output file path without extension
     * @return generated command
     */
    private fun generateNativeCompilationCommand(platform: String, outputFile: File) = arrayOf(
        properties.nativeCompilerPath!!, sourceFile.absolutePath,
        "-nowarn", "-opt",
        "-target", platform,
        "-o", outputFile.absolutePath
    )

    override fun generateCompilationCommand(target: CompilationTarget, outputFile: File) = when (target) {
        CompilationTarget.JVM -> generateJvmCompilationCommand(outputFile)
        CompilationTarget.NATIVE_WINDOWS -> generateNativeCompilationCommand("mingw", outputFile)
        CompilationTarget.NATIVE_OSX     -> generateNativeCompilationCommand("macos", outputFile)
        CompilationTarget.NATIVE_LINUX   -> generateNativeCompilationCommand("linux", outputFile)
    }

    override fun generateInterpretationCommand(target: CompilationTarget) = when (target) {
        CompilationTarget.JVM -> generateJvmInterpretationCommand()
        else -> throw UnsupportedCompilationTargetException(this, target)
    }
}