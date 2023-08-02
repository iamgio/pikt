package eu.iamgio.pikt.eval.kotlin

import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.eval.InjectionData
import eu.iamgio.pikt.lib.Library
import eu.iamgio.pikt.util.stringify

/**
 * [Evaluator] implementation for Kotlin as a target language.
 *
 * @param codeBuilder string builder that contains the generated code
 * @param isInvalidated whether code generation has run into an error
 */
class KotlinEvaluator(codeBuilder: StringBuilder = StringBuilder(), isInvalidated: Boolean = false) : Evaluator(codeBuilder, isInvalidated) {

    override fun clone() = KotlinEvaluator(StringBuilder(codeBuilder), isInvalidated)

    override fun insertInMain() {
        codeBuilder.insert(0, "fun main() {\n")
        codeBuilder.append("\n}")
    }

    override fun generateImport(library: Library): String {
        return "import ${library.info.`package`}.*\n"
    }

    override fun insertInjections(injectionData: InjectionData): Unit = with(injectionData) {
        val code = """
            ${::sourceImagePath.name} = ${sourceImagePath.stringify()}
            
        """.trimIndent()

        codeBuilder.insert(0, code)
    }
}