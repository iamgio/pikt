package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement

/**
 * Used to define and/or set variables' values
 * <%variable%> <name> <value>
 *
 * @author Giorgio Garofalo
 */
class VariableStatement : Statement() {

    override fun getHex(colors: ColorsProperties) = colors.keywords.variable

    override fun generate(reader: PixelReader): String {
        val builder = StringBuilder()

        val name = reader.next()
        if(name == null) {
            reader.error("variable has no name.")
            return ""
        }

        builder.append("var $name=")

        val value = reader.nextExpression()

        // Value is empty for lambda variables, a workaround is needed.
        /*if(value.isEmpty) {
            reader.error("variable has no value.")
        }*/

        builder.append(value.code)

        return builder.toString()
    }
}