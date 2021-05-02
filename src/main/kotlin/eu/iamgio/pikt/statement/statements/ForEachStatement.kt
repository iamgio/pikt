package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement

/**
 * Runs a task for every element of the collection.
 * <%foreach%> <collection> <lambda> <item>
 *
 * @author Giorgio Garofalo
 */
class ForEachStatement : Statement() {

    override fun getColors(colors: ColorsProperties) = colors.keywords.forEach

    override fun generate(reader: PixelReader): String {
        return "(${reader.nextExpression().code} as Iterable<Any>).forEach()"
    }
}