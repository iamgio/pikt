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
abstract class StructStatement : Statement() {

    override val decompactionStyle = DecompactionStyle.SPACE_BEFORE_AND_AFTER

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("struct", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("name", StatementSyntax.Type.OBLIGATORY),
            StatementSyntax.Member("members", StatementSyntax.Type.VARARG_OPTIONAL)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.struct

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence? {
        // The name of the struct
        val name = reader.next()
        if (name == null) {
            syntax.mark("name", StatementSyntax.Mark.WRONG)
            reader.error("Struct has no name.", syntax)
            return null
        }

        val arguments = mutableListOf<Pixel>()

        // The members (parameters) of the struct
        reader.whileNotNull { argument ->
            arguments += argument
            syntax.mark("members", StatementSyntax.Mark.CORRECT)
        }

        data.scope.push(name, StructMember(name, FunctionMember.Overload(arguments.size)))

        return this.generate(name, arguments)
    }

    /**
     * Generates the output code.
     * @param name name of the struct
     * @param arguments members of the struct
     */
    protected abstract fun generate(name: Pixel, arguments: List<Pixel>): CharSequence
}