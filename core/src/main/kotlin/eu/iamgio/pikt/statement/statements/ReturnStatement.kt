package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementSyntax
import eu.iamgio.pikt.statement.statements.bridge.LAMBDA_DEFAULT_BLOCK_NAME

/**
 * Returns a value from a function and finishes its execution.
 * <%return%> <value?>
 *
 * @author Giorgio Garofalo
 */
class ReturnStatement : Statement() {

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("return", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("value", StatementSyntax.Type.OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.`return`

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): String {
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
        return builder.toString()
    }
}