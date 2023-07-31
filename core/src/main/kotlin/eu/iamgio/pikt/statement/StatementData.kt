package eu.iamgio.pikt.statement

import eu.iamgio.pikt.eval.Scope

/**
 * Class that contains information about single, specific statements.
 *
 * @param scope statement scope
 * @param previousStatement statement that comes before, if exists
 * @param nextStatement statement that follows, if exists
 * @param chainSize the amount of chained statements, which can affect the behavior of the statement if [StatementOptions.allowsChaining] is enabled
 * @author Giorgio Garofalo
 */
data class StatementData(
    val scope: Scope,
    val previousStatement: Statement?,
    val nextStatement: Statement?,
    val chainSize: Int,
)
