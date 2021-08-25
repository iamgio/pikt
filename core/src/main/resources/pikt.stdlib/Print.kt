package pikt.stdlib

import pikt.stdlib.targets.jvm.target_printError

/**
 * Prints a message to the [System.out] stream.
 * @param message text content
 */
fun print(message: Any) {
    println(message)
}

/**
 * Prints a message to the [System.err] stream.
 * @param message text content
 */
fun printError(message: Any) {
    target_printError(message)
}

/**
 * Reads an input from command line.
 * It does not work with interpretation.
 * @return input as a string
 */
fun readInput(): String {
    return readLine() ?: ""
}