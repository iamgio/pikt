@file:Suppress("FunctionName")

package pikt.stdlib.targets.native

// Disable IDE error checking for this file!

fun target_printError(message: Any) {
    val STDERR = platform.posix.fdopen(2, "w")
    platform.posix.fprintf(STDERR, message.toString() + "\n")
    platform.posix.fflush(STDERR)
}