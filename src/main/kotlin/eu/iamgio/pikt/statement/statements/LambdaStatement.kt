package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Starts a block of code.
 * <%lambda.open%> <...args?>
 * @author Giorgio Garofalo
 */
class LambdaOpenStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.AFTER

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("lambda.open", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("args", StatementSyntax.Type.VARARG_OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.lambda.open

    override fun generate(reader: PixelReader, syntax: StatementSyntax): String {
        val builder = StringBuilder("{")
        reader.whileNotNull {
            builder.append(it).append(":Any,")
            syntax.mark("args", StatementSyntax.Mark.CORRECT)
        }
        if(builder.endsWith(",")) {
            builder.append("->")
        }
        return builder.toString()
    }
}

/**
 * Closes a block of code.
 * <%lambda.close%>
 * @author Giorgio Garofalo
 */
class LambdaCloseStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.BEFORE_AND_AFTER

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("lambda.close", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
    )

    override fun getColors(colors: ColorsProperties) = colors.lambda.close

    override fun generate(reader: PixelReader, syntax: StatementSyntax) = "}"
}