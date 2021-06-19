package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Runs if its condition is verified.
 * <%if%> <condition> <lambda or statement>
 *
 * @author Giorgio Garofalo
 */
class IfStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.BEFORE

    override val opensTemporaryScope = true

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("if", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("condition", StatementSyntax.Type.OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("lambda|statement", StatementSyntax.Type.OBLIGATORY)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.`if`

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): String {
        val condition = reader.nextExpression(data.scope)
        if(condition.isEmpty) {
            syntax.mark("condition", StatementSyntax.Mark.WRONG)
            reader.error("\"if\" has no condition.", syntax)
        }

        if(data.nextStatement == null || data.nextStatement is ElseStatement) {
            syntax.mark("lambda|statement", StatementSyntax.Mark.WRONG)
            reader.error("\"if\" has empty body.", syntax)
        }

        // Output: if(condition)
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

    override val opensTemporaryScope = true

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("else", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.MemberGroup("ifcondition", StatementSyntax.Type.OPTIONAL, mark = StatementSyntax.Mark.UNSET,
                    StatementSyntax.Member("if", StatementSyntax.Type.SCHEME_OBLIGATORY),
                    StatementSyntax.Member("condition", StatementSyntax.Type.OBLIGATORY)
            ),
            StatementSyntax.Member("lambda|statement", StatementSyntax.Type.OBLIGATORY)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.`else`

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): String {
        /*if(previousStatement !is IfStatement) {
            reader.error("\"else\" has no \"if\".")
        }*/
        // TODO check if 'else' has 'if'

        if(data.nextStatement is IfStatement) {
            syntax.mark("ifcondition", StatementSyntax.Mark.CORRECT)
        } else if(data.nextStatement == null) {
            syntax.mark("lambda|statement", StatementSyntax.Mark.WRONG)
            reader.error("\"else\" has empty body.", syntax)
        }

        // Output: else
        return "else"
    }
}