package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Prints an expression to [System.out].
 * This statement replaces the `print` function from the stdlib, that will be kept for compatibility.
 * <%print%> <value?>
 *
 * @author Giorgio Garofalo
 */
class PrintStatement : Statement() {

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("print", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("value", StatementSyntax.Type.OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.print

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): String {
        val expression = reader.nextExpression(data.scope)

        if(!expression.isEmpty) {
            syntax.mark("value", StatementSyntax.Mark.CORRECT)
        }

        // Output: println(value), or just println() if there is no value
        return "println(${expression.code})"
    }
}