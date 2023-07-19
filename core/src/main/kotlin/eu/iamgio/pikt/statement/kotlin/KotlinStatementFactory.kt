package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.statement.StatementFactory

class KotlinStatementFactory : StatementFactory {

    override fun variableAssignment() = KotlinSetVariableStatement()

    override fun functionCall() = KotlinFunctionCallStatement()

    override fun `if`() = KotlinIfStatement()

    override fun `else`() = KotlinElseStatement()

    override fun struct() = KotlinStructStatement()

    override fun forEach() = KotlinForEachStatement()

    override fun `while`() = KotlinWhileStatement()

    override fun `return`() = KotlinReturnStatement()

    override fun lambdaOpen() = KotlinLambdaOpenStatement()

    override fun lambdaClose() = KotlinLambdaCloseStatement()

    override fun print() = KotlinPrintStatement()
}