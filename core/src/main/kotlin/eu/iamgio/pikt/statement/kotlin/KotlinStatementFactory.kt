package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.statement.StatementFactory
import eu.iamgio.pikt.statement.statements.StructStatement

class KotlinStatementFactory : StatementFactory {

    // TODO implement Kotlin-specific, make general ones abstract

    override fun variableAssignment() = KotlinSetVariableStatement()

    override fun functionCall() = KotlinFunctionCallStatement()

    override fun `if`() = KotlinIfStatement()

    override fun `else`() = KotlinElseStatement()

    override fun struct() = StructStatement()

    override fun forEach() = KotlinForEachStatement()

    override fun `while`() = KotlinWhileStatement()

    override fun `return`() = KotlinReturnStatement()

    override fun lambdaOpen() = KotlinLambdaOpenStatement()

    override fun lambdaClose() = KotlinLambdaCloseStatement()

    override fun print() = KotlinPrintStatement()
}