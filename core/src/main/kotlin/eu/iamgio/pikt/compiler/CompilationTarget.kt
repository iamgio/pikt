package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.GlobalSettings
import eu.iamgio.pikt.command.commands.CMD_INTERPRET
import eu.iamgio.pikt.properties.PiktProperties
import java.io.File

/**
 * Defines system for the executable to run on.
 *
 * @param argName command-line value (target=value)
 * @param commandGenerator generates the commands needed by the Kotlin compiler in order to either create an executable or interpet the source file
 *
 * @author Giorgio Garofalo
 */
enum class CompilationTarget(
        val argName: String,
        val commandGenerator: KotlinCommandGenerator,
) {
    /**
     * Generates a cross-platform .jar executable
     */
    JVM("jvm", object : KotlinCommandGenerator {

        override fun generateCompileCommand(kotlinFile: File, outputFile: File, properties: PiktProperties) = arrayOf(
                properties.jvmCompilerPath!!, kotlinFile.absolutePath, "-nowarn", "-include-runtime", "-d", "$outputFile.jar"
        )

        override fun generateInterpretCommand(kotlinFile: File, properties: PiktProperties) = arrayOf(
                properties.jvmCompilerPath!!, "-script", kotlinFile.absolutePath
        )
    }),

    /**
     * Generates a .exe executable on Windows.
     */
    NATIVE_WINDOWS("windows", object : KotlinCommandGenerator {

        override fun generateCompileCommand(kotlinFile: File, outputFile: File, properties: PiktProperties): Array<String> {
            return generateNativeCompileCommand("mingw", kotlinFile, outputFile, properties)
        }

    }),

    /**
     * Generates a .kexe executable on MacOS.
     */
    NATIVE_OSX("osx", object : KotlinCommandGenerator {

        override fun generateCompileCommand(kotlinFile: File, outputFile: File, properties: PiktProperties): Array<String> {
            return generateNativeCompileCommand("macos", kotlinFile, outputFile, properties)
        }

    }),

    /**
     * Generates a .kexe executable on Linux.
     */
    NATIVE_LINUX("linux", object : KotlinCommandGenerator {

        override fun generateCompileCommand(kotlinFile: File, outputFile: File, properties: PiktProperties): Array<String> {
            return generateNativeCompileCommand("linux", kotlinFile, outputFile, properties)
        }

    });

    /**
     * Whether the target is native.
     */
    val isNative: Boolean
        get() = name.startsWith("NATIVE")

    private companion object {
        /**
         * Generates the command needed by native compilers.
         * @param platform target platform
         * @param kotlinFile temporary .kt source file
         * @param outputFile output file path without extension
         * @param properties Pikt properties
         * @return generated command
         */
        fun generateNativeCompileCommand(platform: String, kotlinFile: File, outputFile: File, properties: PiktProperties) = arrayOf(
            properties.nativeCompilerPath!!, kotlinFile.absolutePath,
                "-nowarn", "-opt", "-target", platform, "-o", outputFile.absolutePath
        )
    }
}

/**
 * Functional interface that allows defining target-specific compilation and interpretation commands.
 */
@FunctionalInterface
interface KotlinCommandGenerator {

    /**
     * Generates the command needed to compile the source file into an executable.
     * @param kotlinFile source .kt file
     * @param outputFile output executable file without extension
     * @param properties Pikt properties
     * @return final command
     * @throws IllegalAccessError if this method has not been implemented by the selected target
     */
    fun generateCompileCommand(kotlinFile: File, outputFile: File, properties: PiktProperties): Array<String> {
        throw IllegalAccessError("This target does not allow compilation.")
    }

    /**
     * Generates the command needed to interpret and run the source file.
     * @param kotlinFile source .kts file
     * @param properties Pikt properties
     * @return final command
     * @throws IllegalAccessError if this method has not been implemented by the selected target
     */
    fun generateInterpretCommand(kotlinFile: File, properties: PiktProperties): Array<String> {
        throw IllegalAccessError("This target does not allow interpretation.")
    }
}

fun List<CompilationTarget?>.isAnyNull(): Boolean   = any { it == null }
fun List<CompilationTarget?>.isAnyJVM(): Boolean    = any { it == CompilationTarget.JVM } || CMD_INTERPRET in GlobalSettings
fun List<CompilationTarget?>.isAnyNative(): Boolean = any { it?.isNative ?: false }