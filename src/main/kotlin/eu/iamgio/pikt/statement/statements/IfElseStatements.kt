package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Runs if its condition is verified.
 * <%if%> <condition> <lambda or statement>
 *
 * @author Giorgio Garofalo
 */
class IfStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.BEFORE

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("if", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("condition", StatementSyntax.Type.OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("lambda|statement", StatementSyntax.Type.OBLIGATORY)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.`if`

    override fun generate(reader: PixelReader, syntax: StatementSyntax): String {
        val condition = reader.nextExpression()
        if(condition.isEmpty) {
            syntax.mark("condition", StatementSyntax.Mark.WRONG)
            reader.error("\"if\" has no condition.", syntax)
        }

        if(nextStatement == null || nextStatement is ElseStatement) {
            syntax.mark("lambda|statement", StatementSyntax.Mark.WRONG)
            reader.error("\"if\" has empty body.", syntax)
        }

        return "if(${condition.code})"
    }
}

/**
 * Runs if the previous [IfStatement] did not run. Can be followed by another [IfStatement] in order to create an `if else` statement.
 * <%else%> <<%if%> <condition>?> <lambda or statement>
 *
 * @author Giorgio Garofalo
 */
class ElseStatement : Statement() {

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("else", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.MemberGroup("ifcondition", StatementSyntax.Type.OPTIONAL, mark = StatementSyntax.Mark.UNSET,
                    StatementSyntax.Member("if", StatementSyntax.Type.SCHEME_OBLIGATORY),
                    StatementSyntax.Member("condition", StatementSyntax.Type.OBLIGATORY)
            ),
            StatementSyntax.Member("lambda|statement", StatementSyntax.Type.OBLIGATORY)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.`else`

    override fun generate(reader: PixelReader, syntax: StatementSyntax): String {
        /*if(previousStatement !is IfStatement) {
            reader.error("\"else\" has no \"if\".")
        } TODO scopes */

        if(nextStatement is IfStatement) {
            syntax.mark("ifcondition", StatementSyntax.Mark.CORRECT)
        } else if(nextStatement == null) {
            syntax.mark("lambda|statement", StatementSyntax.Mark.WRONG)
            reader.error("\"else\" has empty body.", syntax)
        }

        return "else"
    }
}