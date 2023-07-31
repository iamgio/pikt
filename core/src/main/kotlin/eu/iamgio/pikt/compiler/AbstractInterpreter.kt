package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.properties.PiktProperties

/**
 * Abstract interpreter that allows handling process streams.
 *
 * @param evaluator evaluator containing output code
 * @param properties Pikt properties
 * @author Giorgio Garofalo
 */
abstract class AbstractInterpreter(evaluator: Evaluator, properties: PiktProperties) : AbstractCompiler(evaluator, properties) {

    override fun applyEvaluatorSettings() {}

    override fun onPreCompile(target: CompilationTarget) {}

    override fun onPostCompile(target: CompilationTarget) {}
}
