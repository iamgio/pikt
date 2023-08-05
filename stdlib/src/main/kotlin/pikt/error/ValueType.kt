package pikt.error

/**
 * Contains the types a value can be (used for error logging only - not actually used in the language).
 *
 * @author Giorgio Garofalo
 */
object ValueType {
    const val NUMBER = "number"
    const val STRING = "string"
    const val LIST = "list"
    const val FILE = "file"

    infix fun String.or(other: String) = "$this or $other"
}