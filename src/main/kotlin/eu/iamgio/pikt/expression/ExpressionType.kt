package eu.iamgio.pikt.expression

/**
 * Types of expressions
 *
 * @author Giorgio Garofalo
 */
enum class ExpressionType {

    /**
     * A chain of grayscale pixels (1-254).
     * @see eu.iamgio.pikt.image.Pixel.isCharacter
     */
    STRING,

    /**
     * A chain of grayscale pixels (48-57).
     * @see eu.iamgio.pikt.image.Pixel.isNumber
     */
    NUMBER,

    /**
     * Either true (bool.true) or false (bool.false).
     * @see eu.iamgio.pikt.image.Pixel.isBoolean
     */
    BOOLEAN,

    /**
     * A method call where the first pixel is the method name, followed by a pixel for each argument.
     */
    METHOD_CALL,

    /**
     * A mix of the previous expressions containing [Operator]s.
     */
    COMPLEX
}