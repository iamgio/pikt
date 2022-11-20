package eu.iamgio.pikt.image

import eu.iamgio.pikt.GlobalSettings
import eu.iamgio.pikt.command.commands.CMD_PIXELINFO
import eu.iamgio.pikt.expression.Operator
import eu.iamgio.pikt.lib.Libraries
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.properties.ColorsProperty
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.Statements

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
    private val hex: String = color.hex

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
        get() = color.rgb == -1 || !color.isOpaque || matches(colors.whitespace)

    /**
     * Whether this pixel is a string character (grayscale 1-255).
     */
    val isCharacter: Boolean
        get() = !isWhitespace && color.isGrayscale

    /**
     * Whether this pixel is a numeric character (grayscale 48-57).
     */
    val isNumber: Boolean
        get() = isCharacter && color.red >= '0'.code && color.red <= '9'.code

    /**
     * Whether this pixel is a boolean value.
     */
    val isBoolean
        get() = matches(colors.boolean.boolTrue) || matches(colors.boolean.boolFalse)

    /**
     * Whether this pixel is a dot operator (used to access struct data).
     * @see PixelReader.nextSequence
     */
    val isDot
        get() = matches(colors.operators.dot)

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
     * Name of the library member linked to this pixel if exists. `null` otherwise.
     */
    private val libraryMemberName: String? by lazy { Libraries.getMemberName(hex) }

    /**
     * Whether this pixel is linked to a library member/function.
     */
    val isLibraryMember: Boolean
        get() = libraryMemberName != null

    /**
     * Pixel as a Kotlin output content.
     */
    val codeContent: String by lazy {
        when {
            isBoolean -> booleanContent
            isLibraryMember -> libraryMemberName!!
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