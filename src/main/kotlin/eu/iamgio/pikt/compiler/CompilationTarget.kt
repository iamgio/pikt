package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.properties.PiktProperties
import java.io.File

/**
 * Defines system for the executable to run on.
 *
 * @param argName command-line value (target=value)
 * @param compilerCommand command to be executed in order to let the Kotlin compiler generate an executable. (kotlinFile, outputFile, piktProperties) -> command
 *
 * @author Giorgio Garofalo
 */
enum class CompilationTarget(
        val argName: String,
        val compilerCommand: (File, File, PiktProperties) -> String,
) {
    /**
     * Generates a cross-platform .jar executable
     */
    JVM("jvm", { kotlinFile, outputFile, properties ->
        "\"${properties.jvmCompilerPath}\" \"$kotlinFile\" -nowarn -include-runtime -d \"$outputFile.jar\""
    }),

    /**
     * Generates a .exe executable on Windows.
     */
    NATIVE_WINDOWS("windows", {
        kotlinFile, outputFile, properties -> generateNativeCommand("mingw", kotlinFile, outputFile, properties)
    }),

    /**
     * Generates a .kexe executable on MacOS.
     */
    NATIVE_OSX("osx", {
        kotlinFile, outputFile, properties -> generateNativeCommand("macos", kotlinFile, outputFile, properties)
    }),

    /**
     * Generates a .kexe executable on Linux.
     */
    NATIVE_LINUX("linux", {
        kotlinFile, outputFile, properties -> generateNativeCommand("linux", kotlinFile, outputFile, properties)
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
        fun generateNativeCommand(platform: String, kotlinFile: File, outputFile: File, properties: PiktProperties): String {
            return "\"${properties.nativeCompilerPath}\" \"$kotlinFile\" " +
                    "-nowarn -opt -target $platform -o \"$outputFile\""
        }
    }
}

fun List<CompilationTarget?>.isAnyNull(): Boolean                    = any { it == null }
fun List<CompilationTarget?>.isAny(type: CompilationTarget): Boolean = any { type == it }
fun List<CompilationTarget?>.isAnyNative(): Boolean                  = any { it?.isNative ?: false }