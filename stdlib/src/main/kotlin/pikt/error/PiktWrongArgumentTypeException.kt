package pikt.error

/**
 * An exception thrown when a function argument is not of the expected type.
 *
 * @param parameterName name of the wrong parameter
 * @param argumentValue value passed to the parameter
 * @param expectedType the type the function was expecting
 * @param reference a reference object used to retrieve the caller function, usually defined as `object {}`
 * @author Giorgio Garofalo
 */
class PiktWrongArgumentTypeException(
        parameterName: String,
        argumentValue: Any,
        expectedType: String,
        reference: Any,
) : PiktException(
        "Wrong argument type: $parameterName (in ${reference.enclosingMethod.asString})\n" +
                "$parameterName = $argumentValue (JVM type ${argumentValue.jvmType}), " +
                "but $expectedType was expected."
)