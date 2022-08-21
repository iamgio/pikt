package pikt.error

/**
 * An exception thrown when an operation between incompatible types is performed.
 *
 * @param operation the performed operation
 * @param reference a reference object used to retrieve the caller function, usually defined as `object {}`
 * @author Giorgio Garofalo
 */
class PiktInvalidOperationException(operation: String, reference: Any) : PiktException(
        "An illegal operation was attempted: $operation",
        reference
) {

    /**
     * An exception thrown when an operation between incompatible types is performed.
     *
     * @param first first operand
     * @param operator operator symbol
     * @param second second operand
     * @param reference a reference object used to retrieve the caller function, usually defined as `object {}`
     */
    constructor(first: Any, operator: String, second: Any, reference: Any) : this(
            "$first $operator $second (JVM types ${first.jvmType} $operator ${second.jvmType})",
            reference
    )
}