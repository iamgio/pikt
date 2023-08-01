package eu.iamgio.pikt.expression

/**
 * A part of a string.
 */
sealed interface StringComponent

/**
 * A literal character (a, b, 0, 1, X, Y, !, @, etcetera).
 */
data class StringCharacter(val character: Char) : StringComponent

/**
 * A reference to a value within a string.
 */
data class StringReference(val sequence: PixelSequence) : StringComponent

/**
 * Expression that wraps a string value.
 *
 * @param components parts this string is built by
 */
class StringExpression(val components: List<StringComponent>) : Expression(ExpressionType.STRING) {

    override val isEmpty: Boolean
        get() = components.isEmpty()

    override fun toCode(transpiler: ExpressionTranspiler): String {
        return transpiler.string(this)
    }
}