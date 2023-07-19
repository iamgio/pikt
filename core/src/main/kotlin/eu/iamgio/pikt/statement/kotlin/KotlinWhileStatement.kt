package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.statement.statements.WhileStatement
import eu.iamgio.pikt.statement.statements.bridge.LambdaOpenCodeBuilder

/**
 * Kotlin output for [WhileStatement].
 */
class KotlinWhileStatement : WhileStatement() {

    override fun createCodeBuilder(): LambdaOpenCodeBuilder = KotlinWhileLambdaOpenCodeBuilder()

    override fun generate(condition: Expression) = buildString {
        // Output with condition: while ((condition).bool)
        // Output without condition: while (true)

        append("while (")

        if (condition.isEmpty) {
            append("true")
        } else {
            append("(").append(condition.code).append(").bool")
        }

        append(")")
    }
}

/**
 * Defines lambda behavior for while statements.
 */
private class KotlinWhileLambdaOpenCodeBuilder : KotlinDefaultLambdaOpenCodeBuilder() {

    override fun getDelegate() = WhileStatement::class.java

    override fun expectArgsSize(argsSize: Int) = argsSize == 0 // A while loop does not expect arguments
}