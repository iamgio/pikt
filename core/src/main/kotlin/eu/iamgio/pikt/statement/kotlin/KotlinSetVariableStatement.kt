package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.PixelSequence
import eu.iamgio.pikt.expression.kotlin.buildStringWithKotlinTranspiler
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.statements.SetVariableStatement
import eu.iamgio.pikt.statement.statements.bridge.LAMBDA_DEFAULT_BLOCK_NAME
import eu.iamgio.pikt.statement.statements.bridge.LambdaOpenCodeBuilder

/**
 * Kotlin output for [SetVariableStatement].
 */
class KotlinSetVariableStatement : SetVariableStatement() {

    override fun createFunctionDeclarationCodeBuilder(): LambdaOpenCodeBuilder = KotlinFunctionDeclarationLambdaOpenCodeBuilder()

    override fun generate(data: StatementData, sequence: PixelSequence, value: Expression, isFunction: Boolean, isNew: Boolean) = buildStringWithKotlinTranspiler(data.scope) { transpiler ->
        if (isNew) {
            append("var ")
        }

        append(transpiler.sequence(sequence)).append(" = ")

        // If this is a function declaration, name the following block as a Kotlin annotation.
        if(isFunction) {
            append(LAMBDA_DEFAULT_BLOCK_NAME).append("@ ")
        }

        append(value.toCode(transpiler))

        // Output:
        // If variable:
        // [var] name = value
        // If function (including lambda output):
        // [var] name = lambda@ { arg1: Any, arg2: Any ->
    }
}

// This implementation does not serve a real purpose for code generation,
// but it is used by the Return statement in order to recognize whether it is placed within a function declaration.

private class KotlinFunctionDeclarationLambdaOpenCodeBuilder : KotlinDefaultLambdaOpenCodeBuilder() {
    override fun getDelegate() = SetVariableStatement::class.java
}