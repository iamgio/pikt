package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.lib.JarLibrary
import eu.iamgio.pikt.properties.PiktProperties
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
     * @param image Pikt image
     * @param libraries program libraries
     * @see outputCode
     * @see QueuedStatement.eval
     */
    fun evaluate(image: PiktImage, libraries: List<JarLibrary>) {
        val readers = image.reader().subdivide()

        val statements = mutableListOf<QueuedStatement>()

        // Queue statements so that previousStatement and nextStatement can be set.
        for(reader in readers) {
            val pixel = reader.next()
            val statement = pixel?.statement ?: continue

            // Statement chaining changes a statement's behavior
            // depending on the amount of pixels that start it.
            // For example: <return> exits a function,
            // but <return> <return> performs a "break" on a loop.
            if(statement.options.allowsChaining) {
                val last = statements.last()
                // If this is a chained statement, overwrite the last queued statement
                if(last.reader.size == 1 && last.statement::class == statement::class) {
                    statements[statements.size - 1] = last.copy(reader = reader, chainSize = last.chainSize + 1)
                    continue
                }
            }

            // Queue the statement
            statements += QueuedStatement(statement.getEvaluableInstance(), reader)
        }

        // Evaluate queued statements.
        statements.eval(this, Scope.buildMainScope(libraries))
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
     * Imports library functions.
     * @param libraries loaded libraries
     */
    fun insertImports(libraries: List<JarLibrary>) {
        val imports = buildString {
            libraries.forEach { library ->
                append("import ${library.info.`package`}.*\n")
            }
            append("\n")
        }
        codeBuilder.insert(0, imports)
    }

    /**
     * Sets values of variables from the standard library.
     * @param properties Pikt properties
     */
    fun insertInjections(properties: PiktProperties) {
        // Transforms a string into a code-friendly string value
        fun String.stringify() = "\"" + replace("\\", "\\\\") + "\""

        codeBuilder.insert(0, "sourceImagePath = ${properties.source.absolutePath.stringify()}\n")
    }

    /**
     * Invalidates this evaluator (finishes generating Kotlin code but doesn't compile it).
     * @param message optional message to print
     */
    fun invalidate(message: String? = null) {
        isInvalidated = true

        if(message != null) System.err.println("Error: $message\n")
    }
}