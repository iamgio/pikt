package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.eval.ConstantMember
import eu.iamgio.pikt.eval.FunctionMember
import eu.iamgio.pikt.eval.StructMember
import eu.iamgio.pikt.eval.VariableMember
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementSyntax
import eu.iamgio.pikt.statement.statements.bridge.LAMBDA_DEFAULT_BLOCK_NAME

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

        val sequence = reader.nextSequence()
        // A nested variable is a struct field.
        // For instance: a.b.c
        // c is nested inside b (which is nested inside a).
        val isNested = sequence.isNested

        // Name.
        val name = sequence.last
        if(name == null) {
            syntax.mark("name", StatementSyntax.Mark.WRONG)
            reader.error("Variable has no name.", syntax)
            return ""
        }
        syntax.mark("name", StatementSyntax.Mark.CORRECT)

        // Whether this variable represents a function (= is followed by a lambda block).
        val isFunction = data.nextStatement?.isBlock ?: false

        // Check if the variable were already registered.
        // TODO check nested existance
        if(!isNested) when(data.scope[name]) {
            null -> builder.append("var ") // Variable or function is not registered.
            is VariableMember -> {}
            is ConstantMember -> {
                reader.error("${name.hexName} is constant and its value cannot be set.", referenceToFirstPixel = true)
                return ""
            }
            is StructMember -> {
                reader.error("${name.hexName} is already linked to a struct.", referenceToFirstPixel = true)
                return ""
            }
            is FunctionMember -> {
                reader.error("${name.hexName} is a function and its value cannot be set.", referenceToFirstPixel = true)
                return ""
            }
        }

        // Value.
        val value = reader.nextExpression(data.scope)
        if(value.isEmpty && !isFunction) {
            syntax.mark("value", StatementSyntax.Mark.WRONG)
            reader.error("Variable ${name.hexName} has no value.", syntax)
            return ""
        }

        syntax.mark("value", StatementSyntax.Mark.CORRECT)

        // Push variable to the scope.
        if(!reader.isInvalidated && !isNested) {
            if(isFunction) {
                // If this is a function declaration, wait for the next lambda to be evaluated and get the amount of arguments.
                data.nextStatement?.asBlock?.onGenerationCompleted = { args -> data.scope.push(name, FunctionMember(name, FunctionMember.Overload(args.size))) }
            } else {
                // If this a variable declaration, directly push it to the scope.
                data.scope.push(name, VariableMember(name))
            }
        }

        builder.append(sequence).append(" = ")

        // If this is a function declaration, name the following block as a Kotlin annotation.
        if(isFunction) {
            builder.append(LAMBDA_DEFAULT_BLOCK_NAME).append("@ ")
        }

        // Output:
        // If variable:
        // [var] name = value
        // If function (including lambda output):
        // [var] name = lambda@ { arg1: Any, arg2: Any ->
        return builder.append(value.code).toString()
    }
}