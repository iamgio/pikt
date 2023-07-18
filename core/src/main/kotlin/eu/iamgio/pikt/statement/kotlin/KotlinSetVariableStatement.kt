package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.PixelSequence
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.statements.SetVariableStatement
import eu.iamgio.pikt.statement.statements.bridge.LAMBDA_DEFAULT_BLOCK_NAME

/**
 * Kotlin output for [SetVariableStatement].
 */
class KotlinSetVariableStatement : SetVariableStatement() {

    override fun generate(data: StatementData, sequence: PixelSequence, value: Expression, isFunction: Boolean, isNew: Boolean) = buildString {
        if (isNew) {
            append("var ")
        }

        append(sequence.toNestedCode(data.scope)).append(" = ")

        // If this is a function declaration, name the following block as a Kotlin annotation.
        if(isFunction) {
            append(LAMBDA_DEFAULT_BLOCK_NAME).append("@ ")
        }

        append(value.code)

        // Output:
        // If variable:
        // [var] name = value
        // If function (including lambda output):
        // [var] name = lambda@ { arg1: Any, arg2: Any ->
    }
}