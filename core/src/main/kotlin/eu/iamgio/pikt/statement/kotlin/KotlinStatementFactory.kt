package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.statement.StatementFactory

/**
 * Kotlin implementation for [StatementFactory].
 */
class KotlinStatementFactory : StatementFactory {

    override fun variableAssignmentStatement() = KotlinSetVariableStatement()

    override fun functionCallStatement() = KotlinFunctionCallStatement()

    override fun ifStatement() = KotlinIfStatement()

    override fun elseStatement() = KotlinElseStatement()

    override fun structStatement() = KotlinStructStatement()

    override fun forEachStatement() = KotlinForEachStatement()

    override fun whileStatement() = KotlinWhileStatement()

    override fun returnStatement() = KotlinReturnStatement()

    override fun lambdaOpenStatement() = KotlinLambdaOpenStatement()

    override fun lambdaCloseStatement() = KotlinLambdaCloseStatement()

    override fun printStatement() = KotlinPrintStatement()
}