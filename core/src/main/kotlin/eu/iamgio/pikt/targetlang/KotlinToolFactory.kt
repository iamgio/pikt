package eu.iamgio.pikt.targetlang

import eu.iamgio.pikt.eval.KotlinEvaluator
import eu.iamgio.pikt.statement.kotlin.KotlinStatementFactory

/**
 * Kotlin implementation of [TargetLanguageToolFactory].
 */
class KotlinToolFactory : TargetLanguageToolFactory {

    override val statementFactory = KotlinStatementFactory()

    override fun newEvaluator() = KotlinEvaluator()
}