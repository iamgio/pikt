package eu.iamgio.pikt.expression.kotlin

import eu.iamgio.pikt.eval.Scope
import eu.iamgio.pikt.expression.*
import eu.iamgio.pikt.image.Pixel

class KotlinExpressionTranspiler(private val scope: Scope) : ExpressionTranspiler {

    override fun string(expression: StringExpression) = "\"" + expression.components.joinToString("") {
        when (it) {
            is StringCharacter -> it.character.toString()
            is StringReference -> "\${${sequence(it.sequence)}}"
            is StringBlankCharacter -> ""
        }
    } + "\""

    override fun number(expression: StringExpression) = expression.components.joinToString("") {
        when (it) {
            is StringCharacter -> it.character.toString()
            is StringReference -> "" // TODO maybe interpet it as a sum between variables?
            is StringBlankCharacter -> ""
        }
    }

    override fun boolean(expression: BooleanExpression) = expression.content.toString()

    override fun functionCall(expression: FunctionCallExpression) = "".takeIf { expression.isEmpty }
        ?: buildString {
            append(sequence(expression.functionName))
            append("(")

            expression.arguments.forEach { argument ->
                append(argument.toCode(this@KotlinExpressionTranspiler))
                append(", ")
            }

            if (endsWith(", ")) {
                setLength(length - 2)
            }

            append(")")
        }

    override fun structInit(expression: StructInitExpression) = buildString {
        append(symbol(expression.structName))
        append("()") // Default arguments
    }

    override fun operator(operator: Operator): String {
        return " ${operator.symbol} "
    }

    override fun symbol(symbol: Pixel) = when {
        symbol.isBoolean -> symbol.booleanContent.toString()
        symbol.isLibraryMember -> symbol.libraryMemberName!!
        else -> "`${symbol.hex}`"
    }

    /**
     * Converts this sequence of pixels to a Kotlin expression.
     * After the first pixel, each one is treated as a nested member of the previous one.
     * A nested member may be shown either as `first.second` (for structs) or as `first[second]` (for iterables),
     * depending on whether `second` is in the [scope] or not.
     * @return the output code
     */
    override fun sequence(sequence: PixelSequence) = buildString {
        sequence.forEachIndexed { index, pixel ->
            when {
                index == 0        -> append(pixel)
                // If the pixel is in the current scope (or it's a number/character) it is used as index (for lists, strings and more): first[second].
                pixel in scope    -> append("[").append(pixel).append("]")
                pixel.isCharacter -> append("[").append(pixel.characterContent).append("]")
                // If the pixel is not in the current scope, then it's read as a nested property, possibly of a struct: first["second"].
                else              -> append("[\"").append(pixel).append("\"]")
            }
        }
    }
}