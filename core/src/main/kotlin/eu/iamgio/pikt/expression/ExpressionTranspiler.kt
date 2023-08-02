package eu.iamgio.pikt.expression

import eu.iamgio.pikt.image.Pixel

/**
 * A transpiler that converts an [Expression] to some output code.
 */
interface ExpressionTranspiler {

    /**
     * Transpiles a string [expression] into a string value.
     * @return the output code
     */
    fun string(expression: StringExpression): String

    /**
     * Transpiles a string [expression] into a numeric value.
     * @return the output code
     */
    fun number(expression: StringExpression): String

    /**
     * Transpiles a boolean [expression].
     * @return the output code
     */
    fun boolean(expression: BooleanExpression): String

    /**
     * Transpiles a function call [expression].
     * @return the output code
     */
    fun functionCall(expression: FunctionCallExpression): String

    /**
     * Transpiles a struct initialization [expression].
     * @return the output code
     */
    fun structInit(expression: StructInitExpression): String

    /**
     * Transpiles an [operator].
     * @return the output code
     */
    fun operator(operator: Operator): String

    /**
     * Transpiles a single [symbol].
     * @return the output code
     */
    fun symbol(symbol: Pixel): String

    /**
     * Transpiles a [sequence] of nested symbols.
     * @return the output code
     */
    fun sequence(sequence: PixelSequence): String
}