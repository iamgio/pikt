package eu.iamgio.pikt.log.console.color

import eu.iamgio.pikt.image.Color

/**
 * Defines a color palette where a number is linked to a pre-defined color.
 *
 * @author Giorgio Garofalo
 */
interface ColorPalette {

    /**
     * The palette that associates each color index of it with its RGB(A) color, usually lazily.
     */
    val colors: Map<Int, Color>

    /**
     * Calculates the RGB(A) color from a color index.
     * @param colorNumber index of the color within the palette
     * @return RGB(A) color that matches that of the palette at the given index
     * @throws IllegalArgumentException if [colorNumber] is out of the supported palette range
     */
    @Throws(IllegalArgumentException::class)
    fun computeRGBColorFor(colorNumber: Int): Color

    /**
     * Lazily retrieves a index-color map that matches each color index from [start] to [end] to its RGB(A) color.
     */
    fun lazyRangeColorMapping(start: Int, end: Int) = lazy {
        (start..end).associateWith { computeRGBColorFor(it) }
    }
}