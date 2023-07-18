package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementOptions
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Runs if the previous [IfStatement] did not run. Can be followed by another [IfStatement] in order to create an `if else` statement.
 * <%else%> <<%if%> <condition>?> <lambda or statement>
 *
 * @author Giorgio Garofalo
 */
class ElseStatement : Statement() {

    override val options = StatementOptions(opensTemporaryScope = true)

    override fun getSyntax() = StatementSyntax(
        StatementSyntax.Member("else", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
        StatementSyntax.MemberGroup(
            "ifcondition", StatementSyntax.Type.OPTIONAL, mark = StatementSyntax.Mark.UNSET,
            StatementSyntax.Member("if", StatementSyntax.Type.SCHEME_OBLIGATORY),
            StatementSyntax.Member("condition", StatementSyntax.Type.OBLIGATORY)
        ),
        StatementSyntax.Member("lambda|statement", StatementSyntax.Type.OBLIGATORY)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.`else`

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence {
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