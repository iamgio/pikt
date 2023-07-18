package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.statement.statements.PrintStatement

/**
 * Kotlin output for [PrintStatement].
 */
class KotlinPrintStatement : PrintStatement() {

    override fun generate(expression: Expression): CharSequence {
        // Output: println(value), or just println() if there is no value
        return StringBuilder("println(").append(expression.code).append(")")
    }
}