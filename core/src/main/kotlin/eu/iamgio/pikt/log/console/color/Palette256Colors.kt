package eu.iamgio.pikt.log.console.color

import eu.iamgio.pikt.image.Color

// Palette range
private const val BEGIN = 16
private const val GRAYSCALE_BEGIN = 232
private const val END = 255

// Tint (16-231) to RGB
private const val TINT_SCALE = 40
private const val TINT_ADD = 55
private const val RED_DIVIDEND = 36
private const val GREEN_DIVIDEND = 6

// Grayscale (232-255) to RGB
private const val GRAYSCALE_SCALE = 10
private const val GRAYSCALE_ADD = 8

/**
 * Utility class that handles the 256 color palette.
 *
 * @author Giorgio Garofalo
 */
object Palette256Colors {

    /**
     * Color indexes of the 256 color palette (in range 16-255) associated with their RGB color.
     */
    val colors: Map<Int, Color> by lazy {
        (BEGIN..END).associateWith { getRGBColorFor(it) }
    }

    // https://stackoverflow.com/questions/27159322/rgb-values-of-the-colors-in-the-ansi-extended-colors-index-17-255

    /**
     * Computes and retrieves the RGB color of a 256 color.
     * @param colorNumber color index of the 256 color palette in range 16-255
     * @return the RGB color of the 256 color.
     * @throws IllegalArgumentException if [colorNumber] is out of range
     */
    private fun getRGBColorFor(colorNumber: Int): Color {
        return when(colorNumber) {
            in BEGIN until GRAYSCALE_BEGIN -> getRGBColorFromTint(colorNumber)
            in GRAYSCALE_BEGIN..END -> getRGBColorFromGrayscale(colorNumber)
            else -> throw IllegalArgumentException("Color number not in $BEGIN-$END range.")
        }
    }

    /**
     * Computes the RGB color of a 256 color in range 16-231.
     */
    private fun getRGBColorFromTint(colorNumber: Int): Color {
        val relative = colorNumber - BEGIN

        fun getComponent(index: Int) = if(index > 0) TINT_ADD + index * TINT_SCALE else 0

        val red = getComponent(relative / RED_DIVIDEND)
        val green = getComponent(relative % GREEN_DIVIDEND)
        val blue = getComponent((relative % RED_DIVIDEND) / GREEN_DIVIDEND)

        return Color(red, green, blue)
    }

    /**
     * Computes the RGB color of a 256 color in range 232-255.
     */
    private fun getRGBColorFromGrayscale(colorNumber: Int): Color {
        val rgb = (colorNumber - GRAYSCALE_BEGIN) * GRAYSCALE_SCALE + GRAYSCALE_ADD
        return Color.grayscale(rgb)
    }
}