package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement

/**
 * Runs if its condition is verified.
 * <%if%> <condition> <lambda or statement>
 *
 * @author Giorgio Garofalo
 */
class IfStatement : Statement() {

    override fun getColors(colors: ColorsProperties) = colors.keywords.`if`

    override fun generate(reader: PixelReader): String {
        return "if(${reader.nextExpression().code})"
    }
}

/**
 * Runs if the previous [IfStatement] did not run. Can be followed by another [IfStatement] in order to create an <tt>if else</tt> statement.
 * <%else%> <%if%?> <lambda or statement>
 *
 * @author Giorgio Garofalo
 */
class ElseStatement : Statement() {

    override fun getColors(colors: ColorsProperties) = colors.keywords.`else`

    override fun generate(reader: PixelReader) = "else"
}