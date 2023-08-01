package eu.iamgio.pikt.expression

import eu.iamgio.pikt.image.Pixel

/**
 * Expression that wraps a struct initialization with default initial arguments.
 *
 * @param structName name of the struct
 */
class StructInitExpression(val structName: Pixel) : Expression(ExpressionType.FUNCTION_CALL) {

    override val isEmpty: Boolean
        get() = false

    override fun toCode(transpiler: ExpressionTranspiler): String {
        return transpiler.structInit(this)
    }
}