package eu.iamgio.pikt.exit

/**
 * Thrown when closing the process is not possible.
 *
 * @param message exit message
 * @param code exit code
 * @see exit
 * @author Giorgio Garofalo
 */
class ExitAttemptException(message: String?, val code: Int) : Exception(
    "Exit attempt: $message (code $code)"
)