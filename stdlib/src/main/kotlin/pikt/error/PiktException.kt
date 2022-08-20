package pikt.error

import java.lang.reflect.Method

/**
 * A standard exception to be thrown from Pikt libraries.
 *
 * @param message message of the error
 * @author Giorgio Garofalo
 */
open class PiktException(message: String) : RuntimeException(
        "A runtime error was thrown!\n\n$message\n"
) {
    // Reflection utilities
    protected companion object {

        /**
         * Retrieves the caller function from a reference object, usually defined as `object {}`.
         */
        val Any.enclosingMethod: Method
            get() = javaClass.enclosingMethod

        /**
         * The type of a value as a string.
         */
        val Any.jvmType: String
            get() = javaClass.simpleName

        /**
         * Converts a Java reflection method to a string containing the method name and its parameters.
         *
         * Example: `function(param1, param2, param3)`
         */
        val Method.asString: String
            get() = buildString {
                append(name)
                append("(")
                append(parameters.joinToString(separator = ", ") {
                    if(it.isVarArgs) it.name + "..." else it.name
                })
                append(")")
            }
    }
}