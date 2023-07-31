package eu.iamgio.pikt.statement

/**
 * Optional settings of a statement.
 *
 * @param opensScope whether this statement creates a new scope.
 *                   Example: `{` opens a code block
 * @param opensTemporaryScope whether this statement creates a new scope that gets removed on the next statement.
 *                            Example: `if` takes a statement even without a lambda (explicit code block)
 * @param closesScope whether this statement removes its current scope.
 *                    Example: `}` closes a code block
 * @param allowsChaining whether this statement has a different behavior depending on the amount of identificative pixels at its beginning.
 *                             Example: one `return` statement returns a value, two `return`s, one next to the another, perform a `break` on a loop
 * @author Giorgio Garofalo
 */
data class StatementOptions(
        val opensScope: Boolean = false,
        val opensTemporaryScope: Boolean = false,
        val closesScope: Boolean = false,
        val allowsChaining: Boolean = false
) {
    /**
     * Whether at least one of the options modifies the scope status.
     */
    val handlesScopes: Boolean
        get() = opensScope || opensTemporaryScope || closesScope
}
