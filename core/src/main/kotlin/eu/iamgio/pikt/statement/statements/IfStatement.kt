package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementOptions
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Runs if its condition is verified.
 * <%if%> <condition> <lambda or statement>
 *
 * @author Giorgio Garofalo
 */
abstract class IfStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.SPACE_BEFORE
    override val options = StatementOptions(opensTemporaryScope = true)

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("if", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("condition", StatementSyntax.Type.OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("lambda|statement", StatementSyntax.Type.OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.`if`

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence {
        val condition = reader.nextExpression(data.scope)
        if(condition.isEmpty) {
            syntax.mark("condition", StatementSyntax.Mark.WRONG)
            reader.error("\"if\" has no condition.", syntax)
        }

        if(data.nextStatement != null && data.nextStatement !is ElseStatement) {
            syntax.mark("lambda|statement", StatementSyntax.Mark.CORRECT)
        }

        return this.generate(data, condition)
    }

    /**
     * Generates the output code.
     * @param condition condition of the `if` statement
     */
    protected abstract fun generate(data: StatementData, condition: Expression): CharSequence
}
