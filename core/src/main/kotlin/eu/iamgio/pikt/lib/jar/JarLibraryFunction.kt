package eu.iamgio.pikt.lib.jar

import eu.iamgio.pikt.eval.FunctionMember
import eu.iamgio.pikt.lib.LibraryFunction
import java.lang.reflect.Method

/**
 * A function contained within a JAR library as a Java Reflection [Method].
 *
 * @param method corresponding Java Reflection method
 * @author Giorgio Garofalo
 */
class JarLibraryFunction(private val method: Method) : LibraryFunction {

    override val name: String
        get() = method.name

    /**
     * Transforms a Java Reflection method into a Pikt function overload.
     * @return Pikt function overload
     */
    override fun createFunctionOverload(): FunctionMember.Overload {
        return FunctionMember.Overload(
            parameters = method.parameters.map { parameter ->
                FunctionMember.Parameter(
                    parameter.name,
                    parameter.isVarArgs
                )
            },
        )
    }
}