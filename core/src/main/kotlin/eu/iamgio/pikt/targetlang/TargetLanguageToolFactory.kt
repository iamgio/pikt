package eu.iamgio.pikt.targetlang

import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.statement.StatementFactory

/**
 * Factory that gives access to the tools that allow Pikt's execution
 * for a specific target transpilation language.
 */
interface TargetLanguageToolFactory {

    /**
     * Factory to retrieve statements implementations from.
     */
    val statementFactory: StatementFactory

    /**
     * @return a new instance of an empty evaluator for this target language
     */
    fun newEvaluator(): Evaluator
}