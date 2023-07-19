package eu.iamgio.pikt.statement.statements

import eu.iamgio.pikt.eval.ConstantMember
import eu.iamgio.pikt.eval.FunctionMember
import eu.iamgio.pikt.eval.StructMember
import eu.iamgio.pikt.eval.VariableMember
import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.PixelSequence
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.log.pixel.loggableName
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.StatementSyntax
import eu.iamgio.pikt.statement.statements.bridge.DefaultLambdaOpenCodeBuilder

/**
 * Used to define and set (if already they exist) variables
 * <%variable.set%> <name> <value>
 *
 * @author Giorgio Garofalo
 */
abstract class SetVariableStatement : Statement() {

    override fun getSyntax() = StatementSyntax(
            StatementSyntax.Member("variable.set", StatementSyntax.Type.SCHEME_OBLIGATORY, mark = StatementSyntax.Mark.CORRECT),
            StatementSyntax.Member("name", StatementSyntax.Type.OBLIGATORY),
            StatementSyntax.Member("value", StatementSyntax.Type.OBLIGATORY)
    )

    override fun getColors(colors: ColorsProperties) = colors.keywords.setVariable

    override fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence? {
        val sequence = reader.nextSequence()
        // A nested variable is a struct field.
        // For instance: a.b.c
        // c is nested inside b (which is nested inside a).
        val isNested = sequence.isNested

        // Name.
        val name = sequence.lastOrNull()
        if (name == null) {
            syntax.mark("name", StatementSyntax.Mark.WRONG)
            reader.error("Variable has no name.", syntax)
            return null
        }
        syntax.mark("name", StatementSyntax.Mark.CORRECT)

        // Whether this variable represents a function (= is followed by a lambda block).
        val isFunction = data.nextStatement?.isBlock ?: false

        // Whether this variable is new and should be registered.
        val isNew = !isNested && name !in data.scope

        // Check if the variable were already registered to a non-overwritable member type.
        // TODO check nested existance
        if (!isNested) {
            when(data.scope[name]) {
                null -> {}
                is VariableMember -> {}
                is ConstantMember -> {
                    reader.error("${name.loggableName} is constant and its value cannot be set.", referenceToFirstPixel = true)
                    return null
                }
                is StructMember -> {
                    reader.error("${name.loggableName} is already linked to a struct.", referenceToFirstPixel = true)
                    return null
                }
                is FunctionMember -> {
                    reader.error("${name.loggableName} is a function and its value cannot be set.", referenceToFirstPixel = true)
                    return null
                }
            }
        }

        // Value.
        val value = reader.nextExpression(data.scope)
        if (value.isEmpty && !isFunction) {
            syntax.mark("value", StatementSyntax.Mark.WRONG)
            reader.error("Variable ${name.loggableName} has no value.", syntax)
            return null
        }

        syntax.mark("value", StatementSyntax.Mark.CORRECT)

        // Push variable to the scope.
        if (!reader.isInvalidated && !isNested) {
            if (isFunction) {
                val block = data.nextStatement?.asBlock
                // If this is a function declaration, wait for the next lambda to be evaluated and get the amount of arguments.
                block?.onGenerationCompleted = { args -> data.scope.push(name, FunctionMember(name, FunctionMember.Overload(args.size))) }
                block?.codeBuilder = FunctionDeclarationLambdaOpenCodeBuilder()
            } else {
                // If this a variable declaration, directly push it to the scope.
                data.scope.push(name, VariableMember(name))
            }
        }

        return this.generate(data, sequence, value, isFunction, isNew)
    }

    /**
     * Generates the output code.
     * @param data information about this generation
     * @param sequence name of the variable. If it is more than one layer deep, then it is a nested variable
     * @param value value to assign
     * @param isFunction whether this variable represents a function, so that the assignment is a function declaration
     * @param isNew whether this variable is not in scope
     */
    protected abstract fun generate(data: StatementData, sequence: PixelSequence, value: Expression, isFunction: Boolean, isNew: Boolean): CharSequence
}

// This implementation does not serve a real purpose for code generation,
// but it is used by the Return statement in order to recognize whether it is placed within a function declaration.

/**
 * @see ReturnStatement.isReturnPlacementInvalid
 */
class FunctionDeclarationLambdaOpenCodeBuilder : DefaultLambdaOpenCodeBuilder() {
    override fun getDelegate() = SetVariableStatement::class.java
}