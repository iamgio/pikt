package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.eval.Scope
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementOptions
import eu.iamgio.pikt.statement.StatementSyntax
import eu.iamgio.pikt.statement.statements.bridge.LAMBDA_DEFAULT_BLOCK_NAME

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
class ReturnStatement : Statement() {

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
        return scope.isGlobal || !scope.anyParent { it.owner?.isBlock == true && it.owner.asBlock.codeBuilder is FunctionDeclarationLambdaOpenCodeBuilder }
    }

    /**
     * @return whether the statement is placed in a loop
     */
    private fun isBreakPlacementInvalid(scope: Scope): Boolean {
        return scope.isGlobal || !scope.anyParent { it.owner?.isBlock == true && it.owner.asBlock.codeBuilder is ForEachLambdaOpenCodeBuilder } // TODO update when "while" is implemented!
    }

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence? {
        return when(data.chainSize) {
            1 -> generateReturn(reader, syntax, data)
            2 -> generateBreak(reader, syntax, data)
            else -> {
                reader.error("Invalid statement chain size: ${data.chainSize}.")
                null
            }
        }
    }

    private fun generateReturn(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence {
        if(isReturnPlacementInvalid(data.scope)) {
            reader.error("A return statement must be placed within a function declaration.", referenceToFirstPixel = true)
        }

        // The lambda block is identified by its name.
        // In the future it might be dinamically generated,
        // for now it's fixed to "lambda".
        val builder = StringBuilder("return@").append(LAMBDA_DEFAULT_BLOCK_NAME)
        val expression = reader.nextExpression(data.scope)

        if(!expression.isEmpty) {
            syntax.mark("value", StatementSyntax.Mark.CORRECT)
            builder.append(" ").append(expression.code)
        }

        // Output: return@lambda [value]
        return builder
    }

    private fun generateBreak(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence {
        if(isBreakPlacementInvalid(data.scope)) {
            reader.error("A break statement must be placed within a loop.", referenceToFirstPixel = true)
        }

        // A break expects no value.
        if(!reader.nextExpression(data.scope).isEmpty) {
            syntax.mark("value", StatementSyntax.Mark.WRONG)
            reader.error("A break statement cannot have a value.")
        }

        // Output: break
        return "break"
    }
}