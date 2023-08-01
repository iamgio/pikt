package eu.iamgio.pikt.expression

/**
 * Represents an expression that might be a string, a number, a boolean or a complex expression.
 *
 * @param type type of the expression
 * @author Giorgio Garofalo
 */
sealed class Expression(val type: ExpressionType) : ExpressionMember {

    /**
     * Whether this expression has no content.
     */
    abstract val isEmpty: Boolean

    /**
     * Whether this expression has content.
     */
    val isNotEmpty: Boolean
        get() = !isEmpty
}
