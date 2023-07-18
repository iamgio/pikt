package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.statement.statements.ElseStatement

/**
 * Kotlin output for [ElseStatement].
 */
class KotlinElseStatement : ElseStatement() {

    override fun generate(): CharSequence {
        // Output: else
        return "else"
    }
}