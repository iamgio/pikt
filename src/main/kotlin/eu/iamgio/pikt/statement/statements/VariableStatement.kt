package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement

/**
 * Used to define and/or set variables' values
 * <%variable%> <name> <value?>
 *
 * @author Giorgio Garofalo
 */
class VariableStatement : Statement() {

    override fun getHex(colors: ColorsProperties) = colors.variable

    override fun generate(reader: PixelReader): String {
        val builder = StringBuilder()

        val name = reader.next()
        if(name == null) {
            reader.error("variable has no name.")
            return ""
        }

        builder.append("var $name")

        val value = reader.next()
        if(value == null) {
            reader.error("variable has no value.")
            return ""
        }

        builder.append("=$value")

        return builder.toString()
    }
}