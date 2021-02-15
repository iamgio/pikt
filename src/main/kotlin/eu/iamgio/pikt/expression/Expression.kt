package eu.iamgio.pikt.expression

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties

/**
 * Represents an expression that might be a string, a number, a boolean or a complex expression.
 *
 * @param type type of the expression
 * @param code Kotlin code
 * @author Giorgio Garofalo
 */
data class Expression(val type: ExpressionType, val code: String)

/**
 * Parses [Expression]s
 *
 * @param colors colors scheme
 * @param reader pixel reader
 * @author Giorgio Garofalo
 */
class ExpressionParser(private val colors: ColorsProperties, private val reader: PixelReader) {

    /**
     * Analizes the next pixels an finds the expression type.
     * @return type of the expression
     */
    private fun analize(): ExpressionType {
        // Store initial index of the reader in order to roll back after reading the pixels in the expression.
        val startIndex = reader.index

        var type: ExpressionType? = null

        reader.whileNotNull { pixel ->
            type = when {
                // An expression is a string literal if at least one pixel in it is a non-number character.
                // Every non-character in the string will be treated as a variable.
                pixel.isCharacter && !pixel.isNumber -> {
                    ExpressionType.STRING
                }
                // An expression is a number if every pixel in it is an integer.
                (type == null || type == ExpressionType.NUMBER) && pixel.isCharacter && pixel.isNumber -> {
                    ExpressionType.NUMBER
                }
                // An expression is a boolean if its only pixel matches either bool.true or bool.false
                type == null && pixel.isBoolean(colors) -> {
                    ExpressionType.BOOLEAN
                }
                // If the expression is not a string and none of the above match, the expression is complex.
                // Complex expressions need additional evaluations (wip)
                type != ExpressionType.STRING -> {
                    ExpressionType.COMPLEX
                }
                else -> type
            }
        }

        reader.index = startIndex
        return type ?: ExpressionType.COMPLEX
    }

    /**
     * Analyzes and evaluates raw pixels into an expression.
     * @return parsed expression
     */
    fun eval(): Expression {
        val type = analize()

        val code = when(type) {
            ExpressionType.STRING -> "\"${nextString()}\""
            ExpressionType.NUMBER -> nextString(requireNumber = true)
            ExpressionType.BOOLEAN -> if(reader.next()?.matches(colors.boolTrue) == true) "true" else "false"
            ExpressionType.COMPLEX -> "" // TODO
        }

        return Expression(type, code)
    }

    /**
     * @param requireNumber whether a number is expected
     * @return following string literal containing characters and variables
     */
    private fun nextString(requireNumber: Boolean = false): String {
        val builder = StringBuilder()

        reader.whileNotNull { pixel ->
            builder.append(
                    if(pixel.isCharacter && (!requireNumber || pixel.isNumber)) {
                        pixel.characterContent
                    } else {
                        if(requireNumber) {
                            System.err.println("member not expected while parsing number.")
                            ""
                        } else {
                            "\${$pixel}"
                        }
                    }
            )
        }

        return builder.toString()
    }
}