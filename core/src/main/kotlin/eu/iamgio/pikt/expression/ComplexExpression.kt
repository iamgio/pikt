package eu.iamgio.pikt.expression

/**
 *
 */
class ComplexExpression(val members: List<ExpressionMember>) : Expression(ExpressionType.COMPLEX) {

    override val isEmpty: Boolean
        get() = members.isEmpty()

    override fun toCode(transpiler: ExpressionTranspiler) = members.joinToString("") { it.toCode(transpiler) }
}