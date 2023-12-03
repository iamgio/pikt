package pikt.imagelib

import pikt.error.PiktException
import pikt.error.PiktWrongArgumentTypeException
import pikt.error.ValueType.NUMBER
import pikt.stdlib.Struct

// TODO find a better way via color schemes instead of hardcoding colors
private const val RED   = "`E70000`"
private const val GREEN = "`00E700`"
private const val BLUE  = "`0000E7`"

private const val UPPER_LIMIT = 255

/**
 * Representation of an RGB color.
 *
 * @param red the red component in 0-255 value
 * @param green the green component in 0-255 value
 * @param blue the blue component in 0-255 value
 */
class Color(red: Int = 0, green: Int = 0, blue: Int = 0) : Struct(RED to red, GREEN to green, BLUE to blue) {

    /**
     * The red component in 0-255 value
     */
    val red: Int
        get() = this[RED] as Int

    /**
     * The green component in 0-255 value
     */
    val green: Int
        get() = this[GREEN] as Int

    /**
     * The blue component in 0-255 value
     */
    val blue: Int
        get() = this[BLUE] as Int

    override operator fun set(property: Any, value: Any) {
        if (value !is Int) {
            throw PiktWrongArgumentTypeException(
                parameterName = "R/G/B",
                argumentValue = value,
                expectedType = NUMBER,
                reference = object {}
            )
        }

        if (value < 0 || value > UPPER_LIMIT) {
            throw PiktException("0-255 value expected for R/G/B.", reference = object {})
        }

        super.set(property, value)
    }

    override fun toString() = "Color (red=$red, green=$green, blue=$blue)"
}