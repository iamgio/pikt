package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.statement.statements.StructStatement

/**
 * Kotlin output for [StructStatement].
 */
class KotlinStructStatement : StructStatement() {

    override fun generate(name: Pixel, arguments: List<Pixel>) = buildString {
        // Output:
        // class Name : Struct(arg1 to 0, arg2 to 0, ...)

        append("class ").append(name).append(" : Struct(")

        arguments.forEach {
            append("\"").append(it).append("\" to 0, ")
        }

        if (endsWith(", ")) {
            setLength(length - 2)
        }

        append(")")
    }
}