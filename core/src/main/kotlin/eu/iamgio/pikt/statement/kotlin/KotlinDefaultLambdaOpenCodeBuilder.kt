package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.kotlin.KotlinExpressionTranspiler
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.statement.statements.bridge.DefaultLambdaOpenCodeBuilder

/**
 *
 */
open class KotlinDefaultLambdaOpenCodeBuilder : DefaultLambdaOpenCodeBuilder() {

    private val transpiler = KotlinExpressionTranspiler(scope = null)

    // Output:
    //
    // ```
    // { arg1: Any, arg2: Any ->
    // ```

    override fun open() {
        builder.append("{")
    }

    override fun appendArgument(argument: Pixel) {
        builder.append(" ").append(transpiler.symbol(argument)).append(": Any,")
    }

    override fun close() {
        if (builder.endsWith(",")) {
            builder.setCharAt(builder.length - 1, ' ')
            builder.append("->")
        }
    }
}