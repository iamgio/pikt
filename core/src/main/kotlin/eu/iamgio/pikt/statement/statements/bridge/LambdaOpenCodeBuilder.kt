package eu.iamgio.pikt.statement.statements.bridge

import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.statement.statements.LambdaOpenStatement

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
     * Called once at the beginning.
     */
    abstract fun open()

    /**
     * Called once for each argument of the lambda statement.
     * @param argument pixel argument
     */
    abstract fun appendArg(argument: Pixel)

    /**
     * Called once at the end.
     */
    abstract fun close()
}