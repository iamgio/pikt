package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.eval.ConstantMember
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.*
import eu.iamgio.pikt.statement.statements.bridge.LambdaOpenCodeBuilder

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

    /**
     * Whether args should be inserted in the generated code.
     */
    var codeBuilder: LambdaOpenCodeBuilder = DefaultLambdaOpenCodeBuilder()

    override val decompactionStyle = DecompactionStyle.SPACE_AFTER

    override val options = StatementOptions(opensScope = true)

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("lambda.open", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("args", StatementSyntax.Type.VARARG_OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.lambda.open

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): String {
        val arguments = mutableListOf<Pixel>()
        codeBuilder.open()

        // Read arguments one by one (one pixel = one argument)
        reader.whileNotNull { argument ->
            codeBuilder.appendArgument(argument)
            arguments += argument
            // Registers the argument to the scope.
            data.scope.push(argument, ConstantMember(argument))
        }

        codeBuilder.close()

        // Check arguments size
        if(codeBuilder.expectArgsSize(arguments.size)) {
            syntax.mark("args", StatementSyntax.Mark.CORRECT)
        } else {
            syntax.mark("args", StatementSyntax.Mark.WRONG)
            reader.error("Invalid amount of lambda arguments: ${arguments.size} found (delegated by ${codeBuilder.getDelegate().statementName}).", syntax)
        }

        // Invoke the callback
        onGenerationCompleted?.invoke(arguments)

        // When using the default code builder:
        // Output without arguments: {
        // Output with arguments:    { arg1: Any, arg2: Any ->
        // When not using the default code builder,
        // the output depends on its implementation.
        return codeBuilder.code
    }

    override fun getEvaluableInstance() = LambdaOpenStatement()
}

/**
 * Closes a block of code.
 * <%lambda.close%>
 * @author Giorgio Garofalo
 */
class LambdaCloseStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.SPACE_BEFORE_AND_AFTER

    override val options = StatementOptions(closesScope = true)

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("lambda.close", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
    )

    override fun getColors(colors: ColorsProperties) = colors.lambda.close

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData) = "}"
}

/**
 * Defines the default lambda Kotlin-like behavior:
 * ```
 * { arg1: Any, arg2: Any ->
 * ```
 */
class DefaultLambdaOpenCodeBuilder : LambdaOpenCodeBuilder() {
    override fun getDelegate() = LambdaOpenStatement::class.java

    override fun open() {
        builder.append("{")
    }

    override fun appendArgument(argument: Pixel) {
        builder.append(" ").append(argument).append(": Any,")
    }

    override fun close() {
        if(builder.endsWith(",")) {
            builder.setCharAt(builder.length - 1, ' ')
            builder.append("->")
        }
    }
}