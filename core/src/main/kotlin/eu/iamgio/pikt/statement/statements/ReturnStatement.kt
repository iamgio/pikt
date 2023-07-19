package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.eval.Scope
import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.*

/**
 * Performs either a `return` or `break` depending on its chain size:
 *
 * If there is only one statement:
 *
 * Returns a value from a function and finishes its execution.
 * <%return%> <value?>
 *
 * If there are two Return statements, one next to another:
 *
 * Exits a loop.
 * <%return%> <%return%>
 *
 * @author Giorgio Garofalo
 */
abstract class ReturnStatement : Statement() {

    override val options = StatementOptions(allowsChaining = true)

    override fun getSyntax() = StatementSyntax(
        StatementSyntax.Member("return", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
        StatementSyntax.Member("value", StatementSyntax.Type.OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.`return`

    /**
     * @return whether the statement is placed within a function declaration
     */
    private fun isReturnPlacementInvalid(scope: Scope): Boolean {
        return scope.isGlobal || !scope.isInFunctionDeclaration()
    }

    /**
     * @return whether the statement is placed in a loop ([ForEachStatement] or [WhileStatement])
     */
    private fun isBreakPlacementInvalid(scope: Scope): Boolean {
        return scope.isGlobal || !scope.isInLoop()
    }

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence? {
        return when (data.chainSize) {
            1 -> generateReturn(reader, syntax, data)
            2 -> generateBreak(reader, syntax, data)
            else -> {
                reader.error("Invalid statement chain size: ${data.chainSize}.")
                null
            }
        }
    }

    private fun generateReturn(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence {
        if (isReturnPlacementInvalid(data.scope)) {
            reader.error(
                "A return statement must be placed within a function declaration.",
                referenceToFirstPixel = true
            )
        }

        val expression = reader.nextExpression(data.scope)

        return if (expression.isEmpty) {
            this.generateEmptyReturn()
        } else {
            syntax.mark("value", StatementSyntax.Mark.CORRECT)
            this.generateValuedReturn(expression)
        }
    }

    private fun generateBreak(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence {
        if (isBreakPlacementInvalid(data.scope)) {
            reader.error("A break statement must be placed within a loop.", referenceToFirstPixel = true)
        }

        // A break expects no value.
        if (!reader.nextExpression(data.scope).isEmpty) {
            syntax.mark("value", StatementSyntax.Mark.WRONG)
            reader.error("A break statement cannot have a value.")
        }

        return this.generateBreak()
    }

    /**
     * Generates the output code for a `return` with no value.
     */
    protected abstract fun generateEmptyReturn(): CharSequence

    /**
     * Generates the output code for a `return` with a value.
     * @param expression value to return
     */
    protected abstract fun generateValuedReturn(expression: Expression): CharSequence

    /**
     * Generates the output code for a `break` statement within a loop (when the [ReturnStatement] is chained twice).
     */
    protected abstract fun generateBreak(): CharSequence
}