package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.expression.ExpressionType
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement

/**
 * Used to call a method with one pixel per argument without catching the resource value.
 * <%methodcall%> <method> <...args>
 *
 * @author Giorgio Garofalo
 */
class MethodCallStatement : Statement() {

    override fun getHex(colors: ColorsProperties) = colors.keywords.methodCall

    override fun generate(reader: PixelReader) = reader.nextExpression(ExpressionType.METHOD_CALL).code
}