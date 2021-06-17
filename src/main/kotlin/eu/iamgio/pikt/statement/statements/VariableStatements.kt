package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Used to define variables
 * <%variable.define%> <name> <value>
 *
 * @author Giorgio Garofalo
 */
class DefineVariableStatement : Statement() {

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("variable.define", StatementSyntax.Type.SCHEME_OBLIGATORY),
            StatementSyntax.Member("name", StatementSyntax.Type.OBLIGATORY),
            StatementSyntax.Member("value", StatementSyntax.Type.OBLIGATORY)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.defineVariable

    override fun generate(reader: PixelReader, syntax: StatementSyntax): String {
        syntax.mark("variable.define", StatementSyntax.Mark.CORRECT)

        val builder = StringBuilder()

        // Name
        val name = reader.next()
        if(name == null) {
            syntax.mark("name", StatementSyntax.Mark.WRONG)
            reader.error("Variable has no name.", syntax)
            return ""
        }
        syntax.mark("name", StatementSyntax.Mark.CORRECT)

        builder.append("var $name=")

        // Value
        val value = reader.nextExpression()
        if(value.isEmpty && nextStatement !is LambdaOpenStatement) {
            syntax.mark("value", StatementSyntax.Mark.WRONG)
            reader.error("Variable ${name.hexName} has no value.", syntax)
            return ""
        }
        syntax.mark("value", StatementSyntax.Mark.CORRECT)

        builder.append(value.code)

        return builder.toString()
    }
}

/**
 * Used to set variables value
 * <%variable.set%> <name> <value>
 *
 * @author Giorgio Garofalo
 */
class SetVariableStatement : Statement() {

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("variable.set", StatementSyntax.Type.SCHEME_OBLIGATORY),
            StatementSyntax.Member("name", StatementSyntax.Type.OBLIGATORY),
            StatementSyntax.Member("value", StatementSyntax.Type.OBLIGATORY)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.setVariable

    override fun generate(reader: PixelReader, syntax: StatementSyntax): String {
        syntax.mark("variable.set", StatementSyntax.Mark.CORRECT)

        val builder = StringBuilder()

        // Name
        val name = reader.next()
        if(name == null) {
            syntax.mark("name", StatementSyntax.Mark.WRONG)
            reader.error("Variable to update is not defined.", syntax)
            return ""
        }
        syntax.mark("name", StatementSyntax.Mark.CORRECT)

        builder.append("$name=")

        // Value
        val value = reader.nextExpression()
        if(value.isEmpty && nextStatement !is LambdaOpenStatement) {
            syntax.mark("value", StatementSyntax.Mark.WRONG)
            reader.error("No value to set for ${name.hexName} provided.", syntax)
            return ""
        }
        syntax.mark("name", StatementSyntax.Mark.CORRECT)

        builder.append(value.code)

        return builder.toString()
    }
}