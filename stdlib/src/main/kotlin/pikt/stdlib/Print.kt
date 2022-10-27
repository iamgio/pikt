package pikt.stdlib

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
    System.err.println(message)
}

/**
 * Reads a line from the standard input.
 * @return input as a string
 */
fun readInput(): String {
    return readLine() ?: ""
}