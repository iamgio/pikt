package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.ExpressionType
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Used to call a function with one pixel per argument without catching the resource value.
 * <%funcall%> <name> <...args?>
 *
 * @author Giorgio Garofalo
 */
abstract class FunctionCallStatement : Statement() {

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("funcall", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("name", StatementSyntax.Type.OBLIGATORY),
            StatementSyntax.Member("args", StatementSyntax.Type.VARARG_OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.functionCall

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence {
        val expression = reader.nextExpression(data.scope, ExpressionType.FUNCTION_CALL)

        if(expression.isEmpty) {
            syntax.mark("name", StatementSyntax.Mark.WRONG)
            reader.error("No function name provided.", syntax)
        }

        return this.generate(expression)
    }

    /**
     * Generates the output code.
     */
    protected abstract fun generate(function: Expression): CharSequence
}