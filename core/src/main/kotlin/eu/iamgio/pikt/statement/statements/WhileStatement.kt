package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementOptions
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Runs a task for every element of the collection.
 * <%while%> <condition?> <lambda or statement>
 *
 * @author Giorgio Garofalo
 */
class WhileStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.SPACE_BEFORE
    override val options = StatementOptions(opensTemporaryScope = true)

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("while", StatementSyntax.Type.SCHEME_OBLIGATORY, StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("condition", StatementSyntax.Type.OPTIONAL),
            StatementSyntax.Member("lambda|statement", StatementSyntax.Type.OBLIGATORY),
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.`while`

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence? {
        val builder = StringBuilder("while(")
        val condition = reader.nextExpression(data.scope)

        // If no condition was specified, this becomes an endless loop that can be exited from via a 'break'.
        if(condition.isEmpty) {
            builder.append("true")
        } else {
            syntax.mark("condition", StatementSyntax.Mark.CORRECT)
            builder.append("(").append(condition.code).append(").bool")
        }

        builder.append(")")

        // A while loop cannot be the last statement
        if(data.nextStatement == null) {
            syntax.mark("lambda|statement", StatementSyntax.Mark.WRONG)
            reader.error("A while loop must be followed by a code block or statement.", syntax)
            return null
        }

        // If a lambda block follows, give it an identifier for bridging.
        if(data.nextStatement.isBlock) {
            data.nextStatement.asBlock.codeBuilder = WhileLambdaOpenCodeBuilder()
        }

        // Output with condition: while((condition).bool)
        // Output without condition: while(true)
        return builder
    }
}

/**
 * Defines lambda behavior for while statements.
 */
class WhileLambdaOpenCodeBuilder : DefaultLambdaOpenCodeBuilder() {

    override fun getDelegate() = WhileStatement::class.java

    override fun expectArgsSize(argsSize: Int) = argsSize == 0 // A while loop does not expect arguments
}