package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.kotlin.KotlinExpressionTranspiler
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.statements.PrintStatement

/**
 * Kotlin output for [PrintStatement].
 */
class KotlinPrintStatement : PrintStatement() {

    override fun generate(data: StatementData, expression: Expression) = buildString {
        // Output: println(value), or just println() if there is no value
        append("println(")
        append(expression.toCode(KotlinExpressionTranspiler(data.scope)))
        append(")")
    }
}