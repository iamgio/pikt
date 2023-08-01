package eu.iamgio.pikt.expression

/**
 * An expression with no content.
 */
class EmptyExpression : Expression(ExpressionType.EMPTY) {

    override val isEmpty: Boolean
        get() = true

    override fun toCode(transpiler: ExpressionTranspiler) = ""
}