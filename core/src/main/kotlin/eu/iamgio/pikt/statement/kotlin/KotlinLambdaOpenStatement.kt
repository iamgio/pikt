package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.statement.statements.LambdaOpenStatement
import eu.iamgio.pikt.statement.statements.bridge.LambdaOpenCodeBuilder

/**
 * Kotlin output for [LambdaOpenStatement].
 */
class KotlinLambdaOpenStatement : LambdaOpenStatement() {

    override var codeBuilder: LambdaOpenCodeBuilder = KotlinDefaultLambdaOpenCodeBuilder()

    override fun getEvaluableInstance() = KotlinLambdaOpenStatement()
}