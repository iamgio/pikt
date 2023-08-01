package eu.iamgio.pikt.expression

import eu.iamgio.pikt.image.Pixel

/**
 * A transpiler that converts an [Expression] to some output code.
 */
interface ExpressionTranspiler {

    /**
     *
     */
    fun string(expression: StringExpression): String

    fun number(expression: StringExpression): String

    fun boolean(expression: BooleanExpression): String

    fun functionCall(expression: FunctionCallExpression): String

    fun structInit(expression: StructInitExpression): String

    fun operator(operator: Operator): String

    fun symbol(symbol: Pixel): String

    fun sequence(sequence: PixelSequence): String
}