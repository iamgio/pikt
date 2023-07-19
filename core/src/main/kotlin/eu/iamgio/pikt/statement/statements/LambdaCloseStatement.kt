package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementOptions
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Closes a block of code.
 * <%lambda.close%>
 * @author Giorgio Garofalo
 */
abstract class LambdaCloseStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.SPACE_BEFORE_AND_AFTER

    override val options = StatementOptions(closesScope = true)

    override fun getSyntax() = StatementSyntax(
        StatementSyntax.Member("lambda.close", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
    )

    override fun getColors(colors: ColorsProperties) = colors.lambda.close

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData) = this.generate()

    /**
     * Generates the output code.
     */
    protected abstract fun generate(): CharSequence
}
