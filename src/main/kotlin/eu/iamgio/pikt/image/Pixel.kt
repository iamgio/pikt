package eu.iamgio.pikt.image

import eu.iamgio.pikt.expression.Operator
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.Statements
import java.awt.Color

/**
 * Represents a single pixel of a [PiktImage]
 *
 * @param color AWT color of the pixel
 * @param colors color scheme
 * @author Giorgio Garofalo
 */
class Pixel(private val color: Color, val colors: ColorsProperties) {

    /**
     * [color] as hexadecimal.
     */
    private val hex: String = Integer.toHexString(color.rgb).substring(2).toUpperCase()

    /**
     * Whether this pixel is a whitespace (either white or non-opaque), hence should be skipped.
     */
    val isWhitespace: Boolean
        get() = color.rgb == -1 || color.alpha != 255

    /**
     * Whether this pixel is a string character (grayscale 1-255).
     */
    val isCharacter: Boolean
        get() = !isWhitespace && color.red == color.green && color.green == color.blue

    /**
     * Whether this pixel is a numeric character (grayscale 48-57).
     */
    val isNumber: Boolean
        get() = isCharacter && color.red >= '0'.toInt() && color.red <= '9'.toInt()

    /**
     * Whether this pixel is a boolean value
     */
    val isBoolean
        get() = matches(colors.boolean.boolTrue) || matches(colors.boolean.boolFalse)

    /**
     * Character associated to grayscale pixels if [isCharacter] is <tt>true</tt>.
     */
    val characterContent: Char
        get() = color.red.toChar()

    /**
     * Boolean value associated to the pixel if [isBoolean] is <tt>true</tt>.
     */
    val booleanContent: String
        get() = matches(colors.boolean.boolTrue).toString()

    /**
     * [Statement] linked to this pixel if exists. <tt>null</tt> otherwise.
     */
    val statement: Statement?
        get() = Statements.byPixel(this)

    /**
     * Whether this pixel is associated to a statement.
     */
    val hasStatement: Boolean
        get() = statement != null

    /**
     * [Operator] linked to this pixel if exists. <tt>null</tt> otherwise.
     */
    val operator: Operator?
        get() = Operator.byPixel(this)

    /**
     * @param hex hexadecimal color to compare
     * @return whether the pixel's color matches [hex]
     */
    fun matches(hex: String) = this.hex.equals(hex, ignoreCase = true)

    /**
     * @return pixel color as a Kotlin output name
     */
    override fun toString(): String = if(!isBoolean) "`$hex`" else booleanContent
}

/**
 * Represents a collection of [Pixel]s
 *
 * @author Giorgio Garofalo
 */
class PixelArray(private val pixels: Array<Pixel>) {

    /**
     * Size of the array
     */
    val size: Int
        get() = pixels.size

    /**
     * Creates a copy of this array sliced from [start] to [end].
     * @return sliced copy of this array
     */
    fun sliced(start: Int, end: Int): PixelArray = PixelArray(pixels.sliceArray(IntRange(start, end)))

    /**
     * Gets a pixel from the array from given index.
     * @param index item index
     * @return pixel by index
     */
    operator fun get(index: Int): Pixel = pixels[index]

    override fun toString() = "PixelArray(size=${pixels.size}, pixels=[${pixels.joinToString()}])"
}