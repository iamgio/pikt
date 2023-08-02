package eu.iamgio.pikt.statement.kotlin

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.kotlin.KotlinExpressionTranspiler
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.statement.StatementData
import eu.iamgio.pikt.statement.statements.ForEachLambdaOpenCodeBuilder
import eu.iamgio.pikt.statement.statements.ForEachStatement
import eu.iamgio.pikt.statement.statements.bridge.LambdaOpenCodeBuilder

/**
 * Kotlin output for [ForEachStatement].
 */
class KotlinForEachStatement : ForEachStatement() {

    override fun createCodeBuilder(data: StatementData, collection: Expression): LambdaOpenCodeBuilder =
        KotlinForEachLambdaOpenCodeBuilder(collection.toCode(KotlinExpressionTranspiler(data.scope)))

    override fun generate(data: StatementData): CharSequence {
        // Output (including lambda output): for (name in collection) {
        return "for "
    }
}

/**
 * Defines the lambda behavior for Kotlin for-each statements.
 *
 * @param collectionCode code of the collection to iterate.
 */
private class KotlinForEachLambdaOpenCodeBuilder(private val collectionCode: String) : ForEachLambdaOpenCodeBuilder() {

    private val transpiler = KotlinExpressionTranspiler(scope = null)

    // Output:
    //
    // With arguments:
    // ```
    // (arg in collection)
    // ```
    //
    // Without arguments:
    // ```
    // (_ignored_ in collection)
    // ```

    override fun open() {
        builder.append("(")
    }

    override fun appendArgument(argument: Pixel) {
        builder.append(transpiler.symbol(argument))
    }

    override fun close() {
        if (builder.length == 1) {
            builder.append("_ignored_")
        }

        builder.append(" in (").append(collectionCode).append(").iterable").append(") {")
    }
}