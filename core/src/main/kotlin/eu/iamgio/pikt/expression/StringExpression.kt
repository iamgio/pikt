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
 * An empty character that serves no purpose to the content of a string,
 * but may be useful to force a string initialization or to initialize an empty string.
 */
object StringBlankCharacter : StringComponent

/**
 * Expression that wraps a string value.
 *
 * @param components parts this string is built by
 */
class StringExpression(
    val components: List<StringComponent>,
    private val isNumber: Boolean = false
) : Expression(ExpressionType.STRING) {

    override val isEmpty: Boolean
        get() = components.isEmpty()

    override fun toCode(transpiler: ExpressionTranspiler): String {
        return if (isNumber) transpiler.number(this) else transpiler.string(this)
    }
}