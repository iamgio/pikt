package eu.iamgio.pikt.statement.statements.bridge

import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.statements.LambdaOpenStatement

/**
 * Defines the default lambda behavior.
 */
abstract class DefaultLambdaOpenCodeBuilder : LambdaOpenCodeBuilder() {

    override fun getDelegate(): Class<out Statement> = LambdaOpenStatement::class.java
}