package eu.iamgio.pikt.expression

import eu.iamgio.pikt.image.Pixel

/**
 * A transpiler that converts an [Expression] to some output code.
 */
interface ExpressionTranspiler {

    /**
     *
     */
    fun string(content: String): String

    fun number(content: String): String

    fun boolean(content: Boolean): String

    fun functionCall(name: String, arguments: List<String>): String

    fun structInit(name: String, members: List<String>): String

    fun symbol(symbol: Pixel): String

    fun sequence(sequence: PixelSequence): String
}