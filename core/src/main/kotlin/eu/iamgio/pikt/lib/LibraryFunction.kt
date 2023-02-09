package eu.iamgio.pikt.lib

import eu.iamgio.pikt.eval.FunctionMember

/**
 * A function contained within an external library.
 *
 * @author Giorgio Garofalo
 */
interface LibraryFunction {

    /**
     * Name or signature of the function.
     */
    val name: String

    /**
     * @return this function converted to a Pikt function overload
     */
    fun createFunctionOverload(): FunctionMember.Overload
}