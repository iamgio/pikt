package eu.iamgio.pikt.expression

import eu.iamgio.pikt.eval.FunctionMember
import eu.iamgio.pikt.eval.Scope
import eu.iamgio.pikt.eval.ScopeMember
import eu.iamgio.pikt.eval.StructMember
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
        if(this !in scope && !this.isBoolean && !this.isLibraryMember) {
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
        if(memberType != null && !this.isLibraryMember && !typeCheck(memberType)) {
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
                // If there is only one pixel and it is a struct,
                // the expression is a struct initialization
                type == null && scope[pixel] is StructMember -> {
                    ExpressionType.STRUCT_INIT
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
                // An expression is a function call if no character or operator is in it.
                (type == null || type == ExpressionType.FUNCTION_CALL) && !pixel.isCharacter -> {
                    ExpressionType.FUNCTION_CALL
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
            ExpressionType.FUNCTION_CALL -> nextFunctionCall()
            ExpressionType.STRUCT_INIT -> nextStructInit()
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
                    if(pixel.isCharacter) {
                        if(requireNumber && !pixel.isNumber) {
                            reader.error("Member not expected while parsing number.")
                            ""
                        } else {
                            // Grayscale pixel -> character
                            pixel.characterContent
                        }
                    } else {
                        // Variable/method reference
                        pixel.checkExistance(suffix = "(in string literal)")
                        "\${$pixel}"
                    }
            )
        }

        return builder.toString()
    }

    /**
     * Reads a function call: one "name" pixel followed by a pixel for each argument.
     * @return following function call
     */
    private fun nextFunctionCall(): String {
        val builder = StringBuilder()
        val args = mutableListOf<Pixel>()

        // Function name
        val name = reader.next()?.also { name ->
            name.checkExistance()
            builder.append(name)
        }

        builder.append("(")

        // Function arguments
        reader.whileNotNull { pixel ->
            pixel.checkExistance(suffix = "(in function arguments)")
            builder.append(pixel).append("()").append(",")
            args += pixel
        }

        if(builder.endsWith(",")) {
            builder.setLength(builder.length - 1)
        }

        builder.append(")")

        if(args.isNotEmpty()) {
            name?.checkType({ it is FunctionMember }, message = "${name.hexName} is not a valid function.")
        }

        if(name != null) {
            scope[name]?.let { member ->
                if(member is FunctionMember && !member.isApplicableFor(args.size)) {
                    val functionName = (if(member.isLibraryFunction) "${member.name} " else "") + name.hexName
                    val passedArguments = if(args.isNotEmpty()) " (${args.joinToString { it.hexName }})" else ""
                    val argumentsSize = member.overloads.joinToString(" or ") { it.argumentsSize.toString() }
                    reader.error("Function $functionName called with ${args.size} arguments$passedArguments, but $argumentsSize expected.")
                }
            }
        }

        // Return empty string if the method has no name and no arguments.
        return builder.toString().let { if(it == "()") "" else it }
    }

    /**
     * Reads a struct initialization: one pixel matching struct color.
     * @return following struct initialization
     */
    private fun nextStructInit(): String {
        val struct = reader.next()
        if(struct == null) {
            reader.error("Null struct initialization.")
            return ""
        }

        if(scope[struct] !is StructMember) {
            reader.error("Attempted initialization of invalid struct ${struct.hexName}.")
            return ""
        }

        // Return StructName() with zero passed for each argument by default.

        val builder = StringBuilder()
        builder.append(struct).append("(").append(")")
        return builder.toString()
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