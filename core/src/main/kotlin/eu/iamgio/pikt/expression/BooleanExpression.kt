package eu.iamgio.pikt.expression

/**
 * Expression that wraps a boolean value.
 *
 * @param content boolean content
 */
class BooleanExpression(val content: Boolean) : Expression(ExpressionType.BOOLEAN) {

    override val isEmpty: Boolean
        get() = false

    override fun toCode(transpiler: ExpressionTranspiler): String {
        return transpiler.boolean(this)
    }
}