package eu.iamgio.pikt.eval

import eu.iamgio.pikt.lib.Library
import eu.iamgio.pikt.properties.PiktProperties

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

    override fun insertInjections(properties: PiktProperties) {
        // TODO pass key-value injections to this method from this evaluator's superclass
        // in order to make it less error-prone for possible future evaluator implementations

        // Transforms a string into a code-friendly string value
        fun String.stringify() = "\"" + replace("\\", "\\\\") + "\""

        codeBuilder.insert(0, "sourceImagePath = ${properties.source.absolutePath.stringify()}\n")
    }
}