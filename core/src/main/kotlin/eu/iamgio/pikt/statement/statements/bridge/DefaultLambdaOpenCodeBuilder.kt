package eu.iamgio.pikt.statement.statements.bridge

import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.statements.LambdaOpenStatement

/**
 * Defines the default lambda Kotlin-like behavior:
 * ```
 * { arg1: Any, arg2: Any ->
 * ```
 *
 */
open class DefaultLambdaOpenCodeBuilder : LambdaOpenCodeBuilder() {
    override fun getDelegate(): Class<out Statement> = LambdaOpenStatement::class.java

    override fun open() {
        builder.append("{")
    }

    override fun appendArgument(argument: Pixel) {
        builder.append(" ").append(argument).append(": Any,")
    }

    override fun close() {
        if(builder.endsWith(",")) {
            builder.setCharAt(builder.length - 1, ' ')
            builder.append("->")
        }
    }
}