package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.statement.statements.IfStatement

/**
 * Kotlin output for [IfStatement].
 */
class KotlinIfStatement : IfStatement() {

    override fun generate(condition: Expression): CharSequence {
        // Output: if ((condition).bool)
        // Note: bool is a library function from Objects.kt
        return "if ((${condition.code}).bool)"
    }
}