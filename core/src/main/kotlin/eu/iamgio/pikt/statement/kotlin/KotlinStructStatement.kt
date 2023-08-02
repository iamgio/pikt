package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.kotlin.KotlinExpressionTranspiler
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.statements.StructStatement

/**
 * Kotlin output for [StructStatement].
 */
class KotlinStructStatement : StructStatement() {

    override fun generate(data: StatementData, name: Pixel, arguments: List<Pixel>) = buildString {
        // Output:
        // class Name : Struct(arg1 to 0, arg2 to 0, ...)

        val transpiler = KotlinExpressionTranspiler(data.scope)

        append("class ").append(transpiler.symbol(name)).append(" : Struct(")

        arguments.forEach {
            append("\"").append(transpiler.symbol(it)).append("\" to 0, ")
        }

        if (endsWith(", ")) {
            setLength(length - 2)
        }

        append(")")
    }
}