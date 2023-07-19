package eu.iamgio.pikt.statement

import eu.iamgio.pikt.eval.Scope
import eu.iamgio.pikt.statement.statements.ForEachStatement
import eu.iamgio.pikt.statement.statements.LambdaOpenStatement
import eu.iamgio.pikt.statement.statements.SetVariableStatement
import eu.iamgio.pikt.statement.statements.WhileStatement

// Extension functions to check the purpose of a code block.

/**
 * @return whether [this] code block is delegated by a given statement
 */
private fun LambdaOpenStatement.isDelegatedBy(statementClass: Class<out Statement>) = this.codeBuilder.getDelegate().isAssignableFrom(statementClass)

/**
 * @return whether [this] code block is part opens a function declaration body
 */
fun LambdaOpenStatement.isFunctionDeclaration() = isDelegatedBy(SetVariableStatement::class.java)

/**
 * @return whether [this] scope is part of a function declaration
 */
fun Scope.isInFunctionDeclaration() = anyParent { it.owner?.isBlock == true && it.owner.asBlock.isFunctionDeclaration() }

/**
 * @return whether [this] code block opens a for-each loop body
 */
fun LambdaOpenStatement.isForEachLoop() = isDelegatedBy(ForEachStatement::class.java)

/**
 * @return whether [this] code block opens a while loop body
 */
fun LambdaOpenStatement.isWhileLoop() = isDelegatedBy(WhileStatement::class.java)

/**
 * @return whether [this] code block opens a loop body
 */
fun LambdaOpenStatement.isLoop() = isForEachLoop() || isWhileLoop()

/**
 * @return whether [this] scope is part of a loop
 */
fun Scope.isInLoop() = anyParent { it.owner?.isBlock == true && it.owner.asBlock.isLoop() }