package eu.iamgio.pikt.log.console.color

import eu.iamgio.pikt.image.Color
import kotlin.math.abs

/**
 * Allows to approximate a color to its closest match of a color palette.
 *
 * @author Giorgio Garofalo
 */
interface PaletteColorApproximator {

    /**
     * The colors of a [ColorPalette].
     * @see ColorPalette.colors
     */
    val colors: Map<Int, Color>

    /**
     * @param color color to approximate
     * @return the color index (within the palette) whose color is the closest to [color]
     */
    fun getClosestTo(color: Color): Int {
        return colors.entries.minByOrNull { it.value.getSquaredDistanceFrom(color) }!!.key
    }
}

/**
 * Computes how close two colors are. `0` means they are the same color.
 * @param other color to compare
 * @return distance between two colors.
 */
private fun Color.getSquaredDistanceFrom(other: Color): Int {
    return abs(
        (other.red - this.red) * (other.red - this.red) +
                (other.green - this.green) * (other.green - this.green) +
                (other.blue - this.blue) * (other.blue - this.blue)
    )
}