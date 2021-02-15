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
     */
    BOOLEAN,

    /**
     * A mix of the previous expressions.
     */
    COMPLEX
}