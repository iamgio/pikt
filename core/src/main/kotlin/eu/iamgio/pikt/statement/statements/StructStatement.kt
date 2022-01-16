package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.eval.FunctionMember
import eu.iamgio.pikt.eval.StructMember
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Creates a struct.
 * <%struct%> <name> <...members?>
 *
 * @author Giorgio Garofalo
 */
class StructStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.BEFORE_AND_AFTER

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("struct", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("name", StatementSyntax.Type.OBLIGATORY),
            StatementSyntax.Member("members", StatementSyntax.Type.VARARG_OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.struct

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): String {
        val builder = StringBuilder("data class ")

        // The name of the struct
        val name = reader.next()
        if(name == null) {
            syntax.mark("name", StatementSyntax.Mark.WRONG)
            reader.error("Struct has no name.", syntax)
            return builder.toString()
        }

        builder.append(name).append("(")
        val arguments = mutableListOf<Pixel>()

        // The members (parameters) of the struct
        reader.whileNotNull { argument ->
            builder.append("var ").append(argument).append(": Any = 0, ")
            arguments += argument
            syntax.mark("members", StatementSyntax.Mark.CORRECT)
        }
        if(builder.endsWith(", ")) {
            builder.setLength(builder.length - 2)
        }
        builder.append(")")

        data.scope.push(name, StructMember(name, FunctionMember.Overload(arguments.size)))

        // Output: data class Name(var arg1: Any = 0, var arg2: Any = 0, ...)
        return builder.toString()
    }
}