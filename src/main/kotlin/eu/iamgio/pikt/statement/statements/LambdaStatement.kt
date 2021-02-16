package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement

/**
 * Defines a block of code starting by <%lambda.open%>
 * @author Giorgio Garofalo
 */
class LambdaOpenStatement : Statement() {

    override fun getHex(colors: ColorsProperties) = colors.lambda.open

    override fun generate(reader: PixelReader): String {
        val builder = StringBuilder("{")
        reader.whileNotNull {
            builder.append(it).append(",")
        }
        if(builder.endsWith(",")) {
            builder.append("->")
        }
        return builder.toString()
    }
}

/**
 * Defines a block of code ending by <%lambda.close%>
 * @author Giorgio Garofalo
 */
class LambdaCloseStatement : Statement() {

    override fun getHex(colors: ColorsProperties) = colors.lambda.close

    override fun generate(reader: PixelReader) = "}"
}