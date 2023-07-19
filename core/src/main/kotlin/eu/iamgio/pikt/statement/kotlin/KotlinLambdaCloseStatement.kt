package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.statement.statements.LambdaCloseStatement

/**
 * Kotlin output for [LambdaCloseStatement].
 */
class KotlinLambdaCloseStatement : LambdaCloseStatement() {

    override fun generate(): CharSequence {
        // Output: }
        return "}"
    }
}