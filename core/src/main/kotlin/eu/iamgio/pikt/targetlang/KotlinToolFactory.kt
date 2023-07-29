package eu.iamgio.pikt.targetlang

import eu.iamgio.pikt.compiler.kotlin.KotlinCompiler
import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.eval.KotlinEvaluator
import eu.iamgio.pikt.properties.PiktProperties
import eu.iamgio.pikt.statement.kotlin.KotlinStatementFactory

/**
 * Kotlin implementation of [TargetLanguageToolFactory].
 */
class KotlinToolFactory : TargetLanguageToolFactory {

    override val statementFactory = KotlinStatementFactory()

    override fun newEvaluator() = KotlinEvaluator()

    override fun newCompiler(evaluator: Evaluator, properties: PiktProperties) = KotlinCompiler(evaluator, properties)
}