package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementOptions
import eu.iamgio.pikt.statement.StatementSyntax
import eu.iamgio.pikt.statement.statements.bridge.LambdaOpenCodeBuilder

/**
 * Runs a task as long as a condition is verified.
 * <%while%> <condition?> <lambda or statement>
 *
 * @author Giorgio Garofalo
 */
abstract class WhileStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.SPACE_BEFORE
    override val options = StatementOptions(opensTemporaryScope = true)

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("while", StatementSyntax.Type.SCHEME_OBLIGATORY, StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("condition", StatementSyntax.Type.OPTIONAL),
            StatementSyntax.Member("lambda|statement", StatementSyntax.Type.OBLIGATORY),
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.`while`

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence? {
        val condition = reader.nextExpression(data.scope)

        // If no condition was specified, this becomes an endless loop that can be exited from via a 'break'.
        if (!condition.isEmpty) {
            syntax.mark("condition", StatementSyntax.Mark.CORRECT)
        }

        // A while loop cannot be the last statement
        if (data.nextStatement == null) {
            syntax.mark("lambda|statement", StatementSyntax.Mark.WRONG)
            reader.error("A while loop must be followed by a code block or statement.", syntax)
            return null
        }

        // If a lambda block follows, give it an identifier for bridging.
        if(data.nextStatement.isBlock) {
            data.nextStatement.asBlock.codeBuilder = this.createCodeBuilder()
        }

        return this.generate(data, condition)
    }

    /**
     * Instantiates a new [LambdaOpenCodeBuilder] that handles the body of this `while` loop.
     * @return a new lambda code builder for this loop
     */
    protected abstract fun createCodeBuilder(): LambdaOpenCodeBuilder

    /**
     * Generates the output code.
     * @param condition condition of the `while` loop
     */
    protected abstract fun generate(data: StatementData, condition: Expression): CharSequence
}