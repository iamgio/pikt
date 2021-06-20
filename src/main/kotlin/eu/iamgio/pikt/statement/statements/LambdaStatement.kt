package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.*

/**
 * Starts a block of code.
 * <%lambda.open%> <...args?>
 * @author Giorgio Garofalo
 */
class LambdaOpenStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.AFTER
    override val options = StatementOptions(opensScope = true)

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("lambda.open", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("args", StatementSyntax.Type.VARARG_OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.lambda.open

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): String {
        val builder = StringBuilder("{")
        reader.whileNotNull { argument ->
            builder.append(argument).append(": Any,")
            data.scope.push(argument, ScopeMember.Type.CONSTANT)
            syntax.mark("args", StatementSyntax.Mark.CORRECT)
        }
        if(builder.endsWith(",")) {
            builder.setCharAt(builder.length - 1, ' ')
            builder.append("->")
        }

        // Output without arguments: {
        // Output with arguments:    { arg1: Any, arg2: Any ->
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

    override val options = StatementOptions(closesScope = true)

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("lambda.close", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
    )

    override fun getColors(colors: ColorsProperties) = colors.lambda.close

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData) = "}"
}