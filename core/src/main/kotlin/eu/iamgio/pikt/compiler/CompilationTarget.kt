package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.GlobalSettings
import eu.iamgio.pikt.command.commands.CMD_INTERPRET

/**
 * Defines the available platforms/systems for the executables to run on.
 *
 * @param argName command-line value (target=value)
 * @author Giorgio Garofalo
 */
enum class CompilationTarget(val argName: String) {

    /**
     * Generates a cross-platform .jar executable
     */
    JVM("jvm"),

    /**
     * Generates a Windows executable.
     */
    NATIVE_WINDOWS("windows"),

    /**
     * Generates a macOS executable.
     */
    NATIVE_OSX("osx"),

    /**
     * Generates a Linux executable.
     */
    NATIVE_LINUX("linux");

    /**
     * Whether the target is native.
     */
    val isNative: Boolean
        get() = name.startsWith("NATIVE")
}

fun List<CompilationTarget?>.isAnyNull(): Boolean   = any { it == null }
fun List<CompilationTarget?>.isAnyJVM(): Boolean    = any { it == CompilationTarget.JVM } || CMD_INTERPRET in GlobalSettings
fun List<CompilationTarget?>.isAnyNative(): Boolean = any { it?.isNative ?: false }