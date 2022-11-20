package eu.iamgio.pikt.image

private typealias AWTColor = java.awt.Color

/**
 * An immutable RGB(A) color.
 *
 * @param color AWT color
 * @author Giorgio Garofalo
 */
class Color(private val color: AWTColor) {

    /**
     * @param red red component in 0-255 range
     * @param green green component in 0-255 range
     * @param blue blue component in 0-255 range
     */
    constructor(red: Int, green: Int, blue: Int) : this(AWTColor(red, green, blue))

    /**
     * @param rgb combined RGB components
     */
    constructor(rgb: Int) : this(AWTColor(rgb))

    /**
     * The red component in 0-255 range.
     */
    val red: Int
        get() = color.red

    /**
     * The green component in 0-255 range.
     */
    val green: Int
        get() = color.green

    /**
     * The blue component in 0-255 range.
     */
    val blue: Int
        get() = color.blue

    /**
     * The combined RGB components.
     */
    val rgb: Int
        get() = color.rgb

    /**
     * The hexadecimal value of this color, without `#`.
     */
    val hex: String = rgb.rgbToHex()

    /**
     * Whether this color is gray (black, white and in-between).
     */
    val isGrayscale: Boolean
        get() = color.red == color.green && color.green == color.blue

    /**
     * Whether this color is not transparent.
     */
    val isOpaque: Boolean
        get() = color.alpha == 255

    companion object {
        /**
         * The white color.
         */
        val WHITE: Color
            get() = Color(AWTColor.WHITE)

        /**
         * Decodes a [Color] from a hexadecimal string.
         * @param hex hexadecimal value (without `#`)
         * @return corresponding color
         */
        fun fromHex(hex: String) = Color(AWTColor.decode("#$hex"))
    }
}

/**
 * Converts a numeric RGB to its hexadecimal value.
 * @return the corresponding hexadecimal value (without `#`)
 */
fun Int.rgbToHex(): String = Integer.toHexString(this).substring(2).uppercase()