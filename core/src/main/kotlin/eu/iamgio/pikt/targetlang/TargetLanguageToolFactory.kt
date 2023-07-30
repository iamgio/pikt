package eu.iamgio.pikt.targetlang

import eu.iamgio.pikt.compiler.AbstractCompiler
import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.properties.PiktProperties
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

    /**
     * @param evaluator evaluator containing output code
     * @param properties Pikt properties
     * @return a new instance of a compiler that wraps the code content from the [evaluator]
     */
    fun newCompiler(evaluator: Evaluator, properties: PiktProperties): AbstractCompiler

    /**
     * @param evaluator evaluator containing output code
     * @param properties Pikt properties
     * @return a new instance of an interpreter that wraps the code content from the [evaluator]
     */
    fun newInterpreter(evaluator: Evaluator, properties: PiktProperties): AbstractCompiler
}