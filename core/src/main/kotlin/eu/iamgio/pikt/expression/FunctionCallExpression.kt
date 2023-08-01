package eu.iamgio.pikt.expression

/**
 * Expression that wraps a call to a function.
 *
 * @param functionName name of the called function
 * @param arguments expressions to pass to the function as arguments
 */
class FunctionCallExpression(val functionName: PixelSequence, val arguments: List<Expression>) : Expression(ExpressionType.FUNCTION_CALL) {

    override val isEmpty: Boolean
        get() = functionName.isEmpty() && arguments.isEmpty()

    override fun toCode(transpiler: ExpressionTranspiler): String {
        return transpiler.functionCall(this)
    }
}