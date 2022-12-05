package eu.iamgio.pikt.image

private typealias AWTColor = java.awt.Color

/**
 * An immutable RGBA color.
 *
 * @param red the red component in 0-255 value
 * @param green the green component in 0-255 value
 * @param blue the blue component in 0-255 value
 * @param alpha the alpha component in 0-255 value
 * @author Giorgio Garofalo
 */
data class Color(
    val red: Int,
    val green: Int,
    val blue: Int,
    val alpha: Int = 255
) {

    /**
     * @param color AWT color
     */
    constructor(color: AWTColor) : this(color.red, color.green, color.blue, color.alpha)

    /**
     * @param rgb combined RGB components
     */
    constructor(rgb: Int) : this(AWTColor(rgb))

    /**
     * The combined RGB components.
     */
    val rgb: Int = AWTColor(red, green, blue).rgb

    /**
     * The hexadecimal value of this color, without `#`.
     */
    val hex: String = rgb.rgbToHex()

    /**
     * Whether this color is gray (black, white and in-between).
     */
    val isGrayscale: Boolean
        get() = red == green && green == blue

    /**
     * Whether this color is not transparent.
     */
    val isOpaque: Boolean
        get() = alpha == 255

    /**
     * The opposite color.
     */
    val complementary: Color
        get() = Color(
            255 - red,
            255 - green,
            255 - blue,
        )

    companion object {
        /**
         * The white color.
         */
        val WHITE = Color(255, 255, 255)

        /**
         * Decodes a [Color] from a hexadecimal string.
         * @param hex hexadecimal value (without `#`)
         * @return corresponding color
         */
        fun fromHex(hex: String) = Color(AWTColor.decode("#$hex"))

        /**
         * @return a grayscale color where red, green and blue components are equals to [rgb].
         */
        fun grayscale(rgb: Int) = Color(rgb, rgb, rgb)
    }
}

/**
 * Converts a numeric RGB to its hexadecimal value.
 * @return the corresponding hexadecimal value (without `#`)
 */
fun Int.rgbToHex(): String = Integer.toHexString(this).substring(2).uppercase()