package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.statement.statements.FunctionCallStatement

/**
 * Kotlin output for [FunctionCallStatement].
 */
class KotlinFunctionCallStatement : FunctionCallStatement() {

    override fun generate(function: Expression): CharSequence {
        // Output: name(arg1, arg2, ...)
        return function.code
    }
}