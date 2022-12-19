package eu.iamgio.pikt.exit

import eu.iamgio.pikt.log.Log
import kotlin.system.exitProcess

/**
 * Exits the program.
 * If the [NO_EXIT_PROPERTY] system property is set, an exception is thrown instead (mainly for tests).
 * @param code exit code. Any non-zero ([SUCCESS]) code specifies an error
 * @param message optional message to print
 * @throws ExitAttemptException if [NO_EXIT_PROPERTY] is enabled
 */
fun exit(code: Int, message: String? = null): Nothing {
    // The -Dnoexit system property prevents the process from closing
    // and throws an exception instead.
    if(System.getProperty(NO_EXIT_PROPERTY) != null) {
        throw ExitAttemptException(message, code)
    }
    if(message != null) {
        Log.error(message)
    }
    if(code != SUCCESS) {
        Log.error("Exiting. (code $code)")
    }
    exitProcess(code)
}