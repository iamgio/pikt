package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.eval.ConstantMember
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementOptions
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Starts a block of code.
 * <%lambda.open%> <...args?>
 * @author Giorgio Garofalo
 */
class LambdaOpenStatement : Statement() {

    /**
     * A callback that is called once the generation is complete,
     * with a list representing the lambda arguments.
     */
    var onGenerationCompleted: ((List<Pixel>) -> Unit)? = null

    override val decompactionStyle = DecompactionStyle.AFTER
    override val options = StatementOptions(opensScope = true)

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("lambda.open", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("args", StatementSyntax.Type.VARARG_OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.lambda.open

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): String {
        val builder = StringBuilder("{")
        val arguments = mutableListOf<Pixel>()

        reader.whileNotNull { argument ->
            builder.append(" ").append(argument).append(": Any,")
            data.scope.push(argument, ConstantMember(argument))
            arguments += argument
            syntax.mark("args", StatementSyntax.Mark.CORRECT)
        }
        if(builder.endsWith(",")) {
            builder.setCharAt(builder.length - 1, ' ')
            builder.append("->")
        }

        // Invoke the callback
        onGenerationCompleted?.invoke(arguments)

        // Output without arguments: {
        // Output with arguments:    { arg1: Any, arg2: Any ->
        return builder.toString()
    }

    override fun getEvaluableInstance() = LambdaOpenStatement()
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