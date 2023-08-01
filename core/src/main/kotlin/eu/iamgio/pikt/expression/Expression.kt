package eu.iamgio.pikt.expression

/**
 * Represents an expression that might be a string, a number, a boolean or a complex expression.
 *
 * @param type type of the expression
 * @param code Kotlin code
 * @author Giorgio Garofalo
 */
data class Expression(val type: ExpressionType, override val code: String) : ExpressionMember {

    /**
     * Whether this expression has no code.
     */
    val isEmpty: Boolean
        get() = code.isEmpty()

    /**
     * Whether this expression has code.
     */
    val isNotEmpty: Boolean
        get() = code.isNotEmpty()
}
