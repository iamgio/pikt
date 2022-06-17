package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Runs a task for every element of the collection.
 * <%foreach%> <collection> <lambda> <item>
 *
 * @author Giorgio Garofalo
 */
class ForEachStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.BEFORE

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("foreach", StatementSyntax.Type.SCHEME_OBLIGATORY, StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("collection", StatementSyntax.Type.OBLIGATORY),
            StatementSyntax.Member("lambda with argument", StatementSyntax.Type.OBLIGATORY),
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.forEach

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): String {
        val collection = reader.nextExpression(data.scope)

        if(collection.isEmpty) {
            syntax.mark("collection", StatementSyntax.Mark.WRONG)
            reader.error("No list to iterate found.", syntax)
            return ""
        }
        syntax.mark("collection", StatementSyntax.Mark.CORRECT)

        if(data.nextStatement?.isBlock == false) {
            syntax.mark("lambda with argument", StatementSyntax.Mark.WRONG) // TODO check if argument is one
            reader.error("A for-each must be followed by a code block.", syntax)
            return ""
        }
        syntax.mark("lambda with argument", StatementSyntax.Mark.CORRECT)

        // Output: (list).iterable.forEach()
        return "(${collection.code}).iterable.forEach()"
    }
}