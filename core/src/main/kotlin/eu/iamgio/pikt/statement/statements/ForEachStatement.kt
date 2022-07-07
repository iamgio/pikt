package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementSyntax
import eu.iamgio.pikt.statement.statements.bridge.LambdaOpenCodeBuilder

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

        // A following lambda block is required.
        if(data.nextStatement?.isBlock == false) {
            syntax.mark("lambda with argument", StatementSyntax.Mark.WRONG) // TODO check if argument is one
            reader.error("A for-each must be followed by a code block.", syntax)
            return ""
        }
        syntax.mark("lambda with argument", StatementSyntax.Mark.CORRECT)

        // Makes the lambda block that follows generate the correct output.
        data.nextStatement?.asBlock?.codeBuilder = ForEachLambdaOpenCodeBuilder(collection.code)

        // Output (including lambda output): for(name in collection) {
        return "for"
    }
}

/**
 * Defines lambda behavior for for-each statements.
 *
 * With arguments:
 * ```
 * (arg in collection)
 * ```
 *
 * Without arguments:
 * ```
 * (_ignored_ in collection)
 * ```
 *
 * @param collectionCode the code of the collection to iterate.
 */
private class ForEachLambdaOpenCodeBuilder(private val collectionCode: String) : LambdaOpenCodeBuilder() {

    override fun open() {
        builder.append("(")
    }

    override fun appendArg(argument: Pixel) {
        builder.append(argument)
    }

    override fun close() {
        if(builder.length == 1) builder.append("_ignored_")
        builder.append(" in (").append(collectionCode).append(").iterable").append(") {")
    }
}