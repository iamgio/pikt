package pikt.error

/**
 * An exception thrown when an operation produced an I/O error.
 *
 * @param message error message
 * @param reference a reference object used to retrieve the caller function, usually defined as `object {}`
 * @author Giorgio Garofalo
 */
class PiktIOException(message: String, reference: Any) : PiktException("I/O error: $message", reference)