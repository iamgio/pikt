package eu.iamgio.pikt.util

import eu.iamgio.pikt.log.Log
import kotlin.system.exitProcess

// Exit codes that can be used to represent different types of error.

/**
 * Successful execution.
 */
const val SUCCESS = 0

/**
 * One or more Pikt properties are missing or contain a bad value.
 *
 * `-Dproperty=badvalue`
 *
 * @see eu.iamgio.pikt.properties.PiktPropertiesRetriever
 */
const val ERROR_BAD_PROPERTIES = 1

/**
 * One or more program arguments or commands contain a bad value.
 *
 * `argument=badvalue`
 */
const val ERROR_BAD_ARGUMENT_VALUE = 2

/**
 * A task produced an I/O error.
 */
const val ERROR_BAD_IO = 10

/**
 * An image processing task ran into an issue.
 *
 * @see eu.iamgio.pikt.command.commands.imageprocessing
 */
// TODO commands should make more use of this!
const val ERROR_FAILED_IMAGE_PROCESSING = 11

/**
 * Exits the program.
 * @param code exit code. Any non-zero ([SUCCESS]) code specifies an error
 * @param message optional message to print to the [System.err] stream
 */
fun exit(code: Int, message: String? = null): Nothing {
    if(message != null) {
        Log.error(message)
        Log.error("Exiting. (code $code)")
    }
    exitProcess(code)
}