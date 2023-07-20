package eu.iamgio.pikt.eval

import eu.iamgio.pikt.lib.Library

/**
 *
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