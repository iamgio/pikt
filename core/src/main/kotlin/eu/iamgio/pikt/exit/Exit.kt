package eu.iamgio.pikt.exit

import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.SystemPropertyConstants
import kotlin.system.exitProcess

/**
 * Exits the program.
 * If the [SystemPropertyConstants.NO_EXIT] system property is set, an exception is thrown instead (mainly for tests).
 * @param code exit code. Any non-zero ([SUCCESS]) code specifies an error
 * @param message optional message to print
 * @throws ExitAttemptException if [SystemPropertyConstants.NO_EXIT] is enabled
 */
fun exit(code: Int, message: String? = null): Nothing {
    // The -Dnoexit system property prevents the process from closing
    // and throws an exception instead.
    if(System.getProperty(SystemPropertyConstants.NO_EXIT) != null) {
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