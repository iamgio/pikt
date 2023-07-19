package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.statement.StatementFactory
import eu.iamgio.pikt.statement.statements.*

class KotlinStatementFactory : StatementFactory {

    // TODO implement Kotlin-specific, make general ones abstract

    override fun variableAssignment() = KotlinSetVariableStatement()

    override fun functionCall() = FunctionCallStatement()

    override fun `if`() = KotlinIfStatement()

    override fun `else`() = KotlinElseStatement()

    override fun struct() = StructStatement()

    override fun forEach() = ForEachStatement()

    override fun `while`() = WhileStatement()

    override fun `return`() = KotlinReturnStatement()

    override fun lambdaOpen() = LambdaOpenStatement()

    override fun lambdaClose() = KotlinLambdaCloseStatement()

    override fun print() = KotlinPrintStatement()
}