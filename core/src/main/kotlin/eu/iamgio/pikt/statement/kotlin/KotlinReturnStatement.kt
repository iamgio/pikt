package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.kotlin.KotlinExpressionTranspiler
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.statements.ReturnStatement
import eu.iamgio.pikt.statement.statements.bridge.LAMBDA_DEFAULT_BLOCK_NAME

/**
 * Kotlin output for [ReturnStatement].
 */
class KotlinReturnStatement : ReturnStatement() {

    override fun generateEmptyReturn(): StringBuilder {
        // The lambda block is identified by its name.
        // In the future it might be dinamically generated,
        // for now it's fixed to "lambda".

        // Output: return@lambda
        return StringBuilder("return@").append(LAMBDA_DEFAULT_BLOCK_NAME)
    }

    override fun generateValuedReturn(data: StatementData, expression: Expression): CharSequence {
        // Output: return@lambda value
        val value = expression.toCode(KotlinExpressionTranspiler(data.scope))
        return generateEmptyReturn().append(" ").append(value)
    }

    override fun generateBreak(): CharSequence {
        // Output: break
        return "break"
    }
}