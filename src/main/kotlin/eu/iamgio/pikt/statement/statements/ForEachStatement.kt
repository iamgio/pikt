package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
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
            StatementSyntax.Member("lambda", StatementSyntax.Type.OBLIGATORY),
            StatementSyntax.Member("item", StatementSyntax.Type.OBLIGATORY)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.forEach

    override fun generate(reader: PixelReader, syntax: StatementSyntax): String {
        return "(${reader.nextExpression().code} as Iterable<Any>).forEach()"
    }
}