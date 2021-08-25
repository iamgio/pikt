package eu.iamgio.pikt.image

import eu.iamgio.pikt.GlobalSettings
import eu.iamgio.pikt.command.commands.CMD_PIXELINFO
import eu.iamgio.pikt.eval.StdLib
import eu.iamgio.pikt.expression.Operator
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.properties.ColorsProperty
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.Statements
import java.awt.Color

/**
 * @return integer RGB converted into hexadecimal string
 */
fun Int.rgbToHex(): String = Integer.toHexString(this).substring(2).uppercase()

/**
 * Represents a single pixel of a [PiktImage]
 *
 * @param color AWT color of the pixel
 * @param x X coordinate
 * @param y Y coordinate
 * @param colors color scheme
 * @author Giorgio Garofalo
 */
data class Pixel(val color: Color, val x: Int, val y: Int, val colors: ColorsProperties) {

    /**
     * [color] as hexadecimal.
     */
    private val hex: String = color.rgb.rgbToHex()

    /**
     * Used for identification.
     */
    val id: String
        get() = hex

    /**
     * @param colors colors to compare
     * @return whether the pixel's color matches [hex]
     */
    fun matches(colors: ColorsProperty) = colors.has(hex)

    /**
     * Pixel as a hex color, e.g. #FF0000.
     */
    val hexName: String
        get() = "#$hex"

    /**
     * Whether this pixel is a whitespace (either white or non-opaque), hence should be skipped.
     */
    val isWhitespace: Boolean
        get() = color.rgb == -1 || color.alpha != 255 || matches(colors.whitespace)

    /**
     * Whether this pixel is a string character (grayscale 1-255).
     */
    val isCharacter: Boolean
        get() = !isWhitespace && color.red == color.green && color.green == color.blue

    /**
     * Whether this pixel is a numeric character (grayscale 48-57).
     */
    val isNumber: Boolean
        get() = isCharacter && color.red >= '0'.code && color.red <= '9'.code

    /**
     * Whether this pixel is a boolean value
     */
    val isBoolean
        get() = matches(colors.boolean.boolTrue) || matches(colors.boolean.boolFalse)

    /**
     * Character linked to grayscale pixels if [isCharacter] is `true`.
     */
    val characterContent: Char
        get() = color.red.toChar()

    /**
     * Boolean value linked to the pixel if [isBoolean] is `true`.
     */
    val booleanContent: String
        get() = matches(colors.boolean.boolTrue).toString()

    /**
     * [Statement] linked to this pixel if exists. `null` otherwise.
     */
    val statement: Statement? by lazy { Statements.byPixel(this) }

    /**
     * Whether this pixel is linked to a statement.
     */
    val isStatement: Boolean
        get() = statement != null

    /**
     * [Operator] linked to this pixel if exists. `null` otherwise.
     */
    val operator: Operator? by lazy { Operator.byPixel(this) }

    /**
     * Whether this pixel is linked to an operator.
     */
    val isOperator: Boolean
        get() = operator != null

    /**
     * Name of the standard library member linked to this pixel if exists. `null` otherwise.
     */
    private val stdlibMemberName: String? by lazy { StdLib.getMemberName(hex) }

    /**
     * Whether this pixel is linked to a standard library member.
     */
    val isStdlibMember: Boolean
        get() = stdlibMemberName != null

    /**
     * Pixel as a Kotlin output content.
     */
    val codeContent: String by lazy {
        when {
            isBoolean -> booleanContent
            isStdlibMember -> stdlibMemberName!!
            else -> "`$hex`"
        }
    }

    /**
     * @return pixel as a Kotlin output name + pixel information, if enabled.
     */
    override fun toString(): String = codeContent.run {
        // Appends commented pixel coordinates if -pixelinfo is enabled.
        if(CMD_PIXELINFO in GlobalSettings) "$this /*$x,$y*/ " else this
    }
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