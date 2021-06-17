package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement

/**
 * Used to define variables
 * <%variable.define%> <name> <value>
 *
 * @author Giorgio Garofalo
 */
class DefineVariableStatement : Statement() {

    override val syntax: String
        get() = "<%variable.define%> <name> <value>"

    override fun getColors(colors: ColorsProperties) = colors.keywords.defineVariable

    override fun generate(reader: PixelReader): String {
        val builder = StringBuilder()

        val name = reader.next()
        if(name == null) {
            reader.error("Variable has no name.", syntax = true)
            return ""
        }

        builder.append("var $name=")

        val value = reader.nextExpression()

        if(value.isEmpty && nextStatement !is LambdaOpenStatement) {
            reader.error("Variable ${name.hexName} has no value.", syntax = true)
            return ""
        }

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

    override val syntax: String
        get() = "<%variable.set%> <name> <value>"

    override fun getColors(colors: ColorsProperties) = colors.keywords.setVariable

    override fun generate(reader: PixelReader): String {
        val builder = StringBuilder()

        val name = reader.next()
        if(name == null) {
            reader.error("Variable to update is not defined.", syntax = true)
            return ""
        }

        builder.append("$name=")

        val value = reader.nextExpression()
        if(value.isEmpty && nextStatement !is LambdaOpenStatement) {
            reader.error("No value to set for ${name.hexName} provided.", syntax = true)
            return ""
        }

        builder.append(value.code)

        return builder.toString()
    }
}