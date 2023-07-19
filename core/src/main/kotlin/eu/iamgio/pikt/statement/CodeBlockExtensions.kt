package eu.iamgio.pikt.statement

import eu.iamgio.pikt.statement.statements.*

// Extension functions to check the purpose of a code block.

/**
 * @return whether [this] code block is delegated by a given statement
 */
private fun LambdaOpenStatement.isDelegatedBy(statementClass: Class<out Statement>) = this.codeBuilder.getDelegate().isAssignableFrom(statementClass)

/**
 * @return whether a code block is part of a function declaration
 * @see ReturnStatement.isReturnPlacementInvalid
 */
fun LambdaOpenStatement.isFunctionDeclaration() = isDelegatedBy(SetVariableStatement::class.java)

/**
 * @return whether [this] code block is part of a for-each loop
 */
fun LambdaOpenStatement.isForEachLoop() = isDelegatedBy(ForEachStatement::class.java)

/**
 * @return whether [this] code block is part of a while loop
 */
fun LambdaOpenStatement.isWhileLoop() = isDelegatedBy(WhileStatement::class.java)

/**
 * @return whether [this] code block is part of a loop
 * @see ReturnStatement.isBreakPlacementInvalid
 */
fun LambdaOpenStatement.isLoop() = isForEachLoop() || isWhileLoop()