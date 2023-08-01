package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.kotlin.KotlinExpressionTranspiler
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.statements.FunctionCallStatement

/**
 * Kotlin output for [FunctionCallStatement].
 */
class KotlinFunctionCallStatement : FunctionCallStatement() {

    override fun generate(data: StatementData, function: Expression): CharSequence {
        // Output: name(arg1, arg2, ...)
        return function.toCode(KotlinExpressionTranspiler(data.scope))
    }
}