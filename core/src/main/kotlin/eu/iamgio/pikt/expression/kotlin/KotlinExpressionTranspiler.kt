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

    override fun symbol(symbol: Pixel): String {
        return symbol.toString() // TODO use different approach
    }

    override fun sequence(sequence: PixelSequence): String {
        return sequence.toNestedCode(scope) // TODO use different approach
    }
}