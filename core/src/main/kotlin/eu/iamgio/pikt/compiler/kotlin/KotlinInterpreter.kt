package eu.iamgio.pikt.compiler.kotlin

import eu.iamgio.pikt.compiler.Interpreter
import eu.iamgio.pikt.compiler.KOTLIN_COMPILER_ERROR_MESSAGE_HEADER
import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.properties.PiktProperties
import java.io.File

/**
 * Kotlin implementation of [Interpreter].
 */
open class KotlinInterpreter(evaluator: Evaluator, properties: PiktProperties) : Interpreter(evaluator, properties) {

    override val sourceFile = File(outputFolder, properties.output + ".kts")

    override val errorMessageHeader = KOTLIN_COMPILER_ERROR_MESSAGE_HEADER
}