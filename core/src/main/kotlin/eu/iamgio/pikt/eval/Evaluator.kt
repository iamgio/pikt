package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementOptions

/**
 * Evaluates a [PiktImage] in order to generate Kotlin code
 *
 * @param codeBuilder Kotlin code builder
 * @param isInvalidated whether code generation has run into an error
 * @author Giorgio Garofalo
 */
class Evaluator(val codeBuilder: StringBuilder = StringBuilder(), isInvalidated: Boolean = false) : Cloneable {

    /**
     * If an evaluator is invalidated, its content will not be compiled.
     * An evaluator gets invalidate whenever the code generation process runs into an error thrown by [PixelReader.error].
     */
    var isInvalidated = isInvalidated
        private set

    /**
     * Kotlin code output.
     */
    val outputCode: String
        get() = codeBuilder.toString()

    /**
     * @return a copy of this evaluator containing already generated code
     */
    public override fun clone() = Evaluator(StringBuilder(codeBuilder), isInvalidated)

    /**
     * Evaluates [image] source via subdivided pixel readers.
     * @param image pikt image
     * @see outputCode
     * @see QueuedStatement.eval
     */
    fun evaluate(image: PiktImage) {
        val readers = image.reader().subdivide()

        val statements = mutableListOf<QueuedStatement>()

        // Queue statements so that previousStatement and nextStatement can be set.
        readers.forEach { reader ->
            reader.next().let { pixel ->
                pixel?.statement?.let { statements += QueuedStatement(it.getEvaluableInstance(), reader) }
            }
        }

        // Evaluate queued statements
        statements.eval(this)
    }

    /**
     * Appends indentation to [codeBuilder]. The amount of tab characters depends on [Scope.level] and [StatementOptions.handlesScopes].
     * @param scope current scope
     * @param statement target statement
     * @param previousStatement statement that comes before [statement], if exists
     */
    fun appendIndentation(scope: Scope, statement: Statement, previousStatement: Statement?) {
        /**
         * Case 0:              Applied: (nothing changes)
         * statement1           statement1
         * statement2           statement2
         *
         * Case 1:              Applied:
         * var function =       var function =
         *     {                {
         *     statement            statement
         *     }                }
         *
         * Case 2:              Applied:
         * if(condition)        if(condition)
         *     {                {
         *     statement            statement
         * }                    }
         */
        codeBuilder.append(
                "\t".repeat(scope.level - when {
                    statement.options.handlesScopes && previousStatement?.options?.opensTemporaryScope == true -> 2
                    statement.options.handlesScopes -> 1
                    else -> 0
                })
        )
    }

    /**
     * Inserts the current [outputCode] into a `fun main()` block.
     */
    fun insertInMain() {
        codeBuilder.insert(0, "fun main() {\n")
        codeBuilder.append("\n}")
    }

    /**
     * Imports the standard library functions.
     */
    fun insertStdImport() {
        codeBuilder.insert(0, "import pikt.stdlib.*\n\n")
    }

    /**
     * Invalidates this evaluator.
     */
    fun invalidate() {
        isInvalidated = true
    }
}