package eu.iamgio.pikt.statement.statements.bridge

import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.statements.LambdaOpenStatement

/**
 * The name that can be used to identify lambda blocks.
 *
 * It is used as a Kotlin block annotation: `name@ {`
 * allows operations such as `return@name value`
 */
const val LAMBDA_DEFAULT_BLOCK_NAME = "lambda"

/**
 * Defines the way a [LambdaOpenStatement] generates its corresponding output code.
 * This is meant to be used in other statements as a "bridge" between that statement and lambda blocks that follow it.
 *
 * @param builder the String builder that contains the output code for this lambda statement. This is accessed from sub-classes.
 * @see eu.iamgio.pikt.statement.statements.DefaultLambdaOpenCodeBuilder
 * @see eu.iamgio.pikt.statement.statements.ForEachLambdaOpenCodeBuilder
 * @author Giorgio Garofalo
 */
abstract class LambdaOpenCodeBuilder(protected val builder: StringBuilder = StringBuilder()) {

    /**
     * The output code for the [LambdaOpenStatement].
     */
    val code: String
        get() = builder.toString()

    /**
     * @return the class of the statement that handles this [LambdaOpenCodeBuilder] implementation.
     */
    abstract fun getDelegate(): Class<out Statement>

    /**
     * Called once at the beginning.
     */
    abstract fun open()

    /**
     * Called once for each argument of the lambda statement.
     * @param argument pixel argument
     */
    abstract fun appendArgument(argument: Pixel)

    /**
     * Called once at the end.
     */
    abstract fun close()

    /**
     *
     */
    open fun expectArgsSize(argsSize: Int): Boolean = argsSize >= 0 // No way this could be false but better be careful
}