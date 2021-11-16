package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.eval.ConstantMember
import eu.iamgio.pikt.eval.FunctionMember
import eu.iamgio.pikt.eval.VariableMember
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Used to define and set (if already they exist) variables
 * <%variable.set%> <name> <value>
 *
 * @author Giorgio Garofalo
 */
class SetVariableStatement : Statement() {

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("variable.set", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("name", StatementSyntax.Type.OBLIGATORY),
            StatementSyntax.Member("value", StatementSyntax.Type.OBLIGATORY)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.setVariable

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): String {
        val builder = StringBuilder()

        // Name
        val name = reader.next()
        if(name == null) {
            syntax.mark("name", StatementSyntax.Mark.WRONG)
            reader.error("Variable has no name.", syntax)
            return ""
        }
        syntax.mark("name", StatementSyntax.Mark.CORRECT)

        // Check if the variable were already registered
        when(data.scope[name]) {
            null -> builder.append("var ") // Variable is not registered
            is VariableMember -> {}
            is ConstantMember -> {
                reader.error("${name.hexName} is constant and its value cannot be set.", referenceToFirstPixel = true)
                return ""
            }
            is FunctionMember -> {
                reader.error("${name.hexName} is a function and its value cannot be set.", referenceToFirstPixel = true)
                return ""
            }
        }

        // Whether this variable represents a function
        val isFunction = data.nextStatement?.isBlock ?: false

        // Value
        val value = reader.nextExpression(data.scope)
        if(value.isEmpty && !isFunction) {
            syntax.mark("value", StatementSyntax.Mark.WRONG)
            reader.error("Variable ${name.hexName} has no value.", syntax)
            return ""
        }
        syntax.mark("value", StatementSyntax.Mark.CORRECT)

        // Push variable to the scope
        if(!reader.isInvalidated) {
            when(isFunction) {
                // If this is a method declaration, wait for the next lambda to be evaluated and get the amount of arguments
                true -> data.nextStatement?.asBlock?.onGenerationCompleted = { args -> data.scope.push(name, FunctionMember(name, FunctionMember.Overload(args.size))) }
                false -> data.scope.push(name, VariableMember(name))
            }
        }

        // Output: [var] name = value
        return builder.append(name).append(" = ").append(value.code).toString()
    }
}