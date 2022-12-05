package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData

/**
 * This class wraps a [Statement] and its own [PixelReader] and represents a queued evaluation process.
 *
 * @param statement queued statement
 * @param reader statement reader
 * @param chainSize amount of chained statements, if it supports chaining
 * @author Giorgio Garofalo
 */
data class QueuedStatement(val statement: Statement, val reader: PixelReader, val chainSize: Int = 1) {

    /**
     * Opens and closes scopes if required.
     * @param scopes mutable list of scopes
     * @param previousStatement the statement that comes before [statement], if exists
     * @param previousPreviousStatement the statement that comes before [previousStatement], if exists
     */
    fun handleScopes(scopes: MutableList<Scope>, previousStatement: Statement?, previousPreviousStatement: Statement?) {
        // Examples:

        // {        <- opens scope
        //     ...  <- this is in an inner scope
        // }        <- closes scope
        // ...      <- this is in an outer scope

        // if (...) <- no explicit lambda: opens a temporary scope
        //     ...  <- this is in an inner scope
        // ...      <- this is in an outer scope

        if(previousStatement?.options?.closesScope == true || previousPreviousStatement?.options?.opensTemporaryScope == true) {
            scopes.removeLastOrNull() ?: Log.error("There must be at least one active scope.")
        }
        if(statement.options.opensScope || statement.options.opensTemporaryScope) {
            scopes += Scope(parent = scopes.last(), owner = statement)
        }
    }

    /**
     * Generates output Kotlin code for this [statement].
     * @param scopes mutable list of scopes
     * @param evaluator root evaluator
     * @param previousStatement the statement that comes before [statement], if exists
     * @param nextStatement the statement that comes after [statement], if exists
     * @param previousPreviousStatement the statement that comes before [previousStatement], if exists
     */
    fun eval(scopes: MutableList<Scope>, evaluator: Evaluator, previousStatement: Statement?, nextStatement: Statement?, previousPreviousStatement: Statement?) {
        handleScopes(scopes, previousStatement, previousPreviousStatement)
        val scope = scopes.last()

        // Generate code.
        val code = statement.generate(
                reader = reader,
                syntax = statement.getSyntax(),
                data   = StatementData(scope, previousStatement, nextStatement, chainSize)
        )

        // Apply indentation.
        evaluator.appendIndentation(scope, statement, previousStatement)

        // Check if the reader has been invalidated and append generated code.
        if(reader.isInvalidated) {
            evaluator.codeBuilder.append("// Output of ${statement.name} was invalidated. See errors for details. ")
            if(code != null) evaluator.codeBuilder.append("This was generated:\n// ") // Output code gets commented out if invalidated.
            evaluator.invalidate()
        }

        // Append the code generated by this statement.
        if(code != null) evaluator.codeBuilder.append(code)

        // Goes to a new line as long as this statement is not followed by a lambda.
        // This allows, for example, `for (...) {` to be on the same line.
        // Without this check, the output would be:
        // for
        // (...) {
        if(nextStatement?.isBlock == false) evaluator.codeBuilder.append("\n")
    }
}

/**
 * Evaluates and generates code for each [QueuedStatement] of this list.
 * @param evaluator root evaluator (code generator)
 * @param mainScope main global scope
 */
fun List<QueuedStatement>.eval(evaluator: Evaluator, mainScope: Scope) {
    val scopes = mutableListOf(mainScope)

    // Each statement generates its own Kotlin code
    forEachIndexed { index, queued ->
        val previousStatement = elementAtOrNull(index - 1)?.statement
        val nextStatement = elementAtOrNull(index + 1)?.statement
        val previousPreviousStatement = elementAtOrNull(index - 2)?.statement
        queued.eval(scopes, evaluator, previousStatement, nextStatement, previousPreviousStatement)
    }

    // Amount of unclosed scopes.
    val unclosedScopes = getUnclosedScopesAmount(scopes, this)
    if(unclosedScopes > 0) {
        evaluator.invalidate(message = "$unclosedScopes block${if(unclosedScopes != 1) "s are" else " is"} unclosed. Consider closing lambda blocks.")
    }
}

/**
 * Checks for blocks that were not closed properly.
 * @param scopes mutable list of scopes
 * @param statements list of already evaluated queued statements
 * @return amount of unclosed code blocks
 */
private fun getUnclosedScopesAmount(scopes: MutableList<Scope>, statements: List<QueuedStatement>): Int {
    val last = statements.lastOrNull()

    last?.handleScopes(
            scopes,
            previousStatement = last.statement,
            previousPreviousStatement = statements.elementAtOrNull(statements.size - 2)?.statement
    )

    return scopes.size - 1
}