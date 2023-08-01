package eu.iamgio.pikt.expression

/**
 * Interface used to recognize [Expression]s and [Operator]s. Represents a part of complex expressions.
 *
 * @author Giorgio Garofalo
 */
interface ExpressionMember {

    /**
     * Output code
     */
    val code: String
}
