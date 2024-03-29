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
abstract class LambdaOpenStatement : Statement() {

    /**
     * A callback that is called once the generation is complete,
     * that takes a list of lambda arguments.
     */
    var onGenerationCompleted: ((List<Pixel>) -> Unit)? = null

    /**
     * How the block should be handled in the generated code.
     */
    abstract var codeBuilder: LambdaOpenCodeBuilder

    override val decompactionStyle = DecompactionStyle.SPACE_AFTER

    override val options = StatementOptions(opensScope = true)

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("lambda.open", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("args", StatementSyntax.Type.VARARG_OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.lambda.open

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence {
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
        if (codeBuilder.expectArgsSize(arguments.size)) {
            syntax.mark("args", StatementSyntax.Mark.CORRECT)
        } else {
            syntax.mark("args", StatementSyntax.Mark.WRONG)
            reader.error("Invalid amount of lambda arguments: ${arguments.size} found (delegated by ${codeBuilder.getDelegate().statementName}).", syntax)
        }

        // Invoke the callback
        onGenerationCompleted?.invoke(arguments)

        // The output depends on the code builder implementation.
        return codeBuilder.builder
    }
}
