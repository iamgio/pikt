package eu.iamgio.pikt.properties

/**
 * Defines system for the executable to run on.
 *
 * @param argName command-line value (target=value)
 * @param compilerCommand command to be executed in order to let the Kotlin compiler generate an executable. (compilerPath, kotlinPath, targetExecutablePath) -> command
 *
 * @author Giorgio Garofalo
 */
enum class CompilationTarget(
        val argName: String,
        val compilerCommand: (String, String, String) -> String
) {
    /**
     * Generates a cross-platform .jar executable
     */
    JVM("jvm", { compilerPath, kotlinPath, targetExecutablePath -> "$compilerPath $kotlinPath -include-runtime -d $targetExecutablePath.jar" }),

    /**
     * Generates a .exe executable on Windows and a .kexe executable on OSX and Linux
     */
    NATIVE("native", { compilerPath, kotlinPath, targetExecutablePath -> "$compilerPath $kotlinPath -o $targetExecutablePath" })
}