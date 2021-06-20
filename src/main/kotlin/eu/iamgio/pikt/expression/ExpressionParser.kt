package eu.iamgio.pikt.expression

import eu.iamgio.pikt.eval.MethodMember
import eu.iamgio.pikt.eval.Scope
import eu.iamgio.pikt.eval.ScopeMember
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.image.PixelReader

/**
 * Parses [Expression]s
 *
 * @param reader pixel reader
 * @param scope scope where the expression is
 * @param isComplexParser whether this parser is internally generated for complex expression parsing
 * @author Giorgio Garofalo
 */
class ExpressionParser(private val reader: PixelReader, private val scope: Scope, private val isComplexParser: Boolean = false) {

    /**
     * Checks if the pixel is registered within [scope], throws an error otherwise.
     * @param message error message
     * @param suffix text appended to [message]
     */
    private fun Pixel.checkExistance(message: String = "Unresolved reference: $hexName", suffix: String = "") {
        // Cannot check existance for injected stdlib code
        if(this !in scope && !this.isStdlibMember) {
            reader.error(message + (if(suffix.isNotEmpty()) " " else "") + suffix)
        }
    }

    /**
     * Checks if the pixel is registered as a given type within [scope], throws an error otherwise.
     * @param typeCheck expected member type function
     * @param message error message
     */
    private fun Pixel.checkType(typeCheck: (ScopeMember) -> Boolean, message: String) {
        val memberType = scope[this]
        if(memberType != null && !this.isStdlibMember && !typeCheck(memberType)) {
            reader.error(message)
        }
    }

    /**
     * Analizes the next pixels an finds the expression type.
     * @return type of the expression
     */
    private fun analyze(): ExpressionType {
        // Store initial index of the reader in order to roll back after reading the pixels in the expression.
        val startIndex = reader.index

        var type: ExpressionType? = null
        reader.whileNotNull { pixel ->
            type = when {
                // A complex expression must remain complex
                type == ExpressionType.COMPLEX -> {
                    type
                }
                // If the expression is not a string and none of the above match, the expression is complex.
                // Complex expressions need additional evaluations.
                pixel.isOperator -> {
                    ExpressionType.COMPLEX
                }
                // An expression is a string literal if at least one pixel in it is a non-number character.
                // Every non-character in the string will be treated as a variable.
                pixel.isCharacter && !pixel.isNumber -> {
                    ExpressionType.STRING
                }
                // An expression is a number if every pixel in it is an integer.
                (type == null || type == ExpressionType.NUMBER) && pixel.isCharacter && pixel.isNumber -> {
                    ExpressionType.NUMBER
                }
                // An expression is a boolean if its only pixel matches either bool.true or bool.false.
                type == null && pixel.isBoolean -> {
                    ExpressionType.BOOLEAN
                }
                // An expression is a method call if no character or operator is in it.
                (type == null || type == ExpressionType.METHOD_CALL) && !pixel.isCharacter -> {
                    ExpressionType.METHOD_CALL
                }
                else -> type
            }
        }

        reader.index = startIndex
        return type ?: ExpressionType.COMPLEX
    }

    /**
     * Analyzes and evaluates raw pixels into an expression.
     * @param type forced expression type. Dynamic if not specified
     * @return parsed expression
     */
    fun eval(type: ExpressionType? = null): Expression {
        val expressionType = type ?: analyze()

        val code = when(expressionType) {
            ExpressionType.STRING -> "\"${nextString()}\""
            ExpressionType.NUMBER -> nextString(requireNumber = true)
            ExpressionType.BOOLEAN -> reader.next()?.booleanContent ?: "false"
            ExpressionType.METHOD_CALL -> nextMethodCall()
            ExpressionType.COMPLEX -> nextComplex()
        }

        return Expression(expressionType, code)
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
                            reader.error("Member not expected while parsing number.")
                            ""
                        } else {
                            pixel.checkExistance("(in string literal)")
                            "\${$pixel}"
                        }
                    }
            )
        }

        return builder.toString()
    }

    /**
     * Reads a method call: one "name" pixel followed by a pixel for each argument.
     * @return following method call
     */
    private fun nextMethodCall(): String {
        val builder = StringBuilder()
        var hasArgs = false

        // Method name
        val name = reader.next()?.also { name ->
            name.checkExistance()
            builder.append(name)
        }

        builder.append("(")

        // Method arguments
        reader.whileNotNull { pixel ->
            pixel.checkExistance(suffix = "(in method arguments)")
            builder.append(pixel).append("()").append(",")
            hasArgs = true
        }

        if(builder.endsWith(",")) {
            builder.setLength(builder.length - 1)
        }

        if(hasArgs) {
            name?.checkType({ it is MethodMember }, message = "${name.hexName} is not a valid method.")
        }

        return builder.append(")").toString()
    }

    /**
     * Splits a complex expression into simple expressions.
     * @return subdivision of the complex expression into minor expressions and operators
     */
    private fun splitComplex(): List<ExpressionMember> {
        val members = mutableListOf<ExpressionMember>()

        var startIndex = reader.index

        while(true) {
            val pixel = reader.next()
            val operator = pixel?.operator

            if(!(isComplexParser && startIndex <= 0) && (pixel == null || operator != null)) {
                members += ExpressionParser(reader.sliced(startIndex, reader.index - 1).also { it.next() }, scope, isComplexParser = true).eval()
                startIndex = reader.index

                if(operator != null) {
                    members += operator
                }
            }
            if(pixel == null) return members
        }
    }

    /**
     * Reads the following complex expression and converts it to Kotlin code
     * @return Kotlin code of complex expression
     */
    private fun nextComplex(): String {
        return splitComplex().joinToString("") { it.code }
    }
}