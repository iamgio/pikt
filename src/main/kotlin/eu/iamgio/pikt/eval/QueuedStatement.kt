package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.statement.Scope
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData

/**
 * This class wraps a [Statement] and its own [PixelReader] and represents a queued evaluation process.
 *
 * @param statement queued statement
 * @param reader statement reader
 * @author Giorgio Garofalo
 */
data class QueuedStatement(val statement: Statement, val reader: PixelReader) {

    /**
     * Opens and closes scopes if required.
     * @param scopes mutable list of scopes
     * @param previousStatement the statement that comes before [statement]
     * @param previousPreviousStatement the statement that comes before [previousStatement], if exists
     */
    private fun handleScopes(scopes: MutableList<Scope>, previousStatement: Statement?, previousPreviousStatement: Statement?) {
        if(previousStatement?.closesScope == true || previousPreviousStatement?.opensTemporaryScope == true) {
            scopes.removeLastOrNull() ?: System.err.println("There must be at least one active scope.")
        }
        if(statement.opensScope || statement.opensTemporaryScope) {
            scopes += Scope(parent = scopes.last())
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

        // Generate and append code.
        val code = statement.generate(reader, statement.getSyntax(), StatementData(scopes.last(), previousStatement, nextStatement))
        if(reader.isInvalidated) {
            evaluator.codeBuilder.append("// Output of ${statement.name} was invalidated. See errors for details.\n")
            evaluator.invalidate()
        } else {
            evaluator.codeBuilder.append(code).append("\n")
        }
    }
}

/**
 * Evaluates and generates code for each [QueuedStatement].
 * @param evaluator root evaluator
 */
fun List<QueuedStatement>.eval(evaluator: Evaluator) {
    val scopes = mutableListOf(Scope(parent = null))

    forEachIndexed { index, queued ->
        val previousStatement = elementAtOrNull(index - 1)?.statement
        val nextStatement = elementAtOrNull(index + 1)?.statement
        val previousPreviousStatement = elementAtOrNull(index - 2)?.statement
        queued.eval(scopes, evaluator, previousStatement, nextStatement, previousPreviousStatement)
    }
}