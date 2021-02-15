package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.properties.PiktProperties
import java.io.File

/**
 * Defines system for the executable to run on.
 *
 * @param argName command-line value (target=value)
 * @param compilerCommand command to be executed in order to let the Kotlin compiler generate an executable. (kotlinFile, piktProperties) -> command
 *
 * @author Giorgio Garofalo
 */
enum class CompilationTarget(
        val argName: String,
        val compilerCommand: (File, PiktProperties) -> String
) {
    /**
     * Generates a cross-platform .jar executable
     */
    JVM("jvm", { kotlinFile, properties -> "\"${properties.jvmCompilerPath}\" \"$kotlinFile\" -nowarn -version -include-runtime -d \"${properties.output}.jar\"" }),

    /**
     * Generates a .exe executable on Windows and a .kexe executable on OSX and Linux
     */
    NATIVE("native", { kotlinFile, properties -> "\"${properties.nativeCompilerPath}\" \"$kotlinFile\" -nowarn -version -o \"${properties.output}\"" })
}