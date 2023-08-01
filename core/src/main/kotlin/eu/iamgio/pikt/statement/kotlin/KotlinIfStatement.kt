package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.kotlin.KotlinExpressionTranspiler
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.statements.IfStatement

/**
 * Kotlin output for [IfStatement].
 */
class KotlinIfStatement : IfStatement() {

    override fun generate(data: StatementData, condition: Expression): CharSequence {
        // Output: if ((condition).bool)
        // Note: bool is a library function from Objects.kt
        val conditionCode = condition.toCode(KotlinExpressionTranspiler(data.scope))
        return "if (($conditionCode).bool)"
    }
}