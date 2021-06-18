package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.expression.ExpressionType
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Used to call a method with one pixel per argument without catching the resource value.
 * <%methodcall%> <name> <...args?>
 *
 * @author Giorgio Garofalo
 */
class MethodCallStatement : Statement() {

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("methodcall", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("name", StatementSyntax.Type.OBLIGATORY),
            StatementSyntax.Member("args", StatementSyntax.Type.VARARG_OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.methodCall

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): String {
        val expression = reader.nextExpression(ExpressionType.METHOD_CALL)

        if(expression.isEmpty || expression.code == "()") {
            syntax.mark("name", StatementSyntax.Mark.WRONG)
            reader.error("No method name provided.", syntax)
        }

        return expression.code
    }
}