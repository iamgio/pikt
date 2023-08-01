package eu.iamgio.pikt.expression.kotlin

import eu.iamgio.pikt.expression.ExpressionTranspiler
import eu.iamgio.pikt.expression.PixelSequence
import eu.iamgio.pikt.image.Pixel

class KotlinExpressionTranspiler : ExpressionTranspiler {

    override fun string(content: String): String {
        return "\"$content\""
    }

    override fun number(content: String): String {
        return content
    }

    override fun boolean(content: Boolean): String {
        TODO("Not yet implemented")
    }

    override fun functionCall(name: String, arguments: List<String>): String {
        TODO("Not yet implemented")
    }

    override fun structInit(name: String, members: List<String>): String {
        TODO("Not yet implemented")
    }

    override fun symbol(symbol: Pixel): String {
        TODO("Not yet implemented")
    }

    override fun sequence(sequence: PixelSequence): String {
        TODO("Not yet implemented")
    }
}