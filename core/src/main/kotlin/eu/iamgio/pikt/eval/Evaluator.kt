package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.lib.Libraries
import eu.iamgio.pikt.lib.Library
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.evaluateIndentationLevel

/**
 * Evaluates a [PiktImage] in order to generate output code
 *
 * @param codeBuilder string builder that contains the generated code
 * @param isInvalidated whether code generation has run into an error
 * @author Giorgio Garofalo
 */
abstract class Evaluator(val codeBuilder: StringBuilder = StringBuilder(), isInvalidated: Boolean = false) : Cloneable {

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
    public abstract override fun clone(): Evaluator

    /**
     * Evaluates [image] source via subdivided pixel readers.
     * @param image Pikt image
     * @param mainScope the main scope of the program which contains library symbols
     * @see outputCode
     * @see Scope.buildMainScope
     * @see QueuedStatement.eval
     */
    fun evaluate(image: PiktImage, mainScope: Scope) {
        val readers = image.reader().subdivide()

        val statements = mutableListOf<QueuedStatement>()

        // Queue statements so that previousStatement and nextStatement can be set.
        for (reader in readers) {
            val pixel = reader.next()
            val statement = pixel?.statement ?: continue

            // Statement chaining changes a statement's behavior
            // depending on the amount of pixels that start it.
            // For example: <return> exits a function,
            // but <return> <return> performs a "break" on a loop.
            if (statement.options.allowsChaining && statements.isNotEmpty()) {
                val last = statements.last()
                // If this is a chained statement, overwrite the last queued statement
                if (last.reader.size == 1 && last.statement::class == statement::class) {
                    statements[statements.size - 1] = last.copy(reader = reader, chainSize = last.chainSize + 1)
                    continue
                }
            }

            // Queue the statement
            statements += QueuedStatement(statement.getEvaluableInstance(), reader)
        }

        // Evaluate queued statements.
        statements.eval(this, mainScope)
    }

    /**
     * Appends indentation to [codeBuilder].
     * The amount of tabulation characters depends on [Statement.evaluateIndentationLevel].
     *
     * ```
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
     * if (condition)       if (condition)
     *     {                {
     *     statement            statement
     * }                    }
     * ```
     *
     * @param scope current scope
     * @param statement statement to calculate the indentation level for
     * @param previousStatement statement that preceeds [statement], if it exists
     * @see Statement.evaluateIndentationLevel
     */
    fun appendIndentation(scope: Scope, statement: Statement, previousStatement: Statement?) {
        val indentationLevel = statement.evaluateIndentationLevel(scope, previousStatement)
        codeBuilder.append("\t".repeat(indentationLevel))
    }

    /**
     * Inserts the current [outputCode] into a `main` executable block.
     */
    abstract fun insertInMain()

    /**
     * @param library library to import
     * @return an import line to access [library] functions.
     */
    protected abstract fun generateImport(library: Library): String

    /**
     * Imports library functions.
     * @param libraries loaded libraries
     */
    fun insertImports(libraries: Libraries) {
        val imports = buildString {
            libraries.forEach { library ->
                append(generateImport(library))
            }
            append("\n")
        }
        codeBuilder.insert(0, imports)
    }

    /**
     * Inserts informational variables into the output code.
     * @param injectionData information to inject
     */
    abstract fun insertInjections(injectionData: InjectionData)

    /**
     * Invalidates this evaluator (finishes generating Kotlin code but doesn't compile it).
     * @param message optional message to print
     */
    fun invalidate(message: String? = null) {
        isInvalidated = true

        if (message != null) {
            Log.error("Error: $message\n")
        }
    }
}