package eu.iamgio.pikt.log.console.color

import eu.iamgio.pikt.image.Color

// Palette range
private const val BEGIN = 0
private const val END = 15

// VGA color values (an RGB component can be one of these)
private const val VGA_NONE = 0
private const val VGA_LOW = 85
private const val VGA_HALF = 170
private const val VGA_FULL = 255

/**
 * Utility class that handles the ANSI 16 color palette.
 *
 * @author Giorgio Garofalo
 */
object Ansi16ColorPalette : ColorPalette, PaletteColorApproximator {

    /**
     * Color indexes of the 16 color palette (in range 0-15) associated with their RGB color.
     */
    override val colors: Map<Int, Color> by super.lazyRangeColorMapping(BEGIN, END)

    /**
     * Retrieves the RGB color of an ANSI 16 color.
     * @param colorNumber color index of the color palette in range 0-15
     * @return the RGB color of the ANSI 16 color.
     * @throws IllegalArgumentException if [colorNumber] is out of range
     */
    override fun computeRGBColorFor(colorNumber: Int): Color {
        return when(colorNumber) {
            // VGA values. Maybe they could be automated?
            VGA_NONE -> Color.grayscale(VGA_NONE)       // Black
            1 -> Color(VGA_HALF, VGA_NONE, VGA_NONE)    // Red
            2 -> Color(VGA_NONE, VGA_HALF, VGA_NONE)    // Green
            3 -> Color(VGA_HALF, VGA_LOW, VGA_NONE)     // Yellow
            4 -> Color(VGA_NONE, VGA_NONE, VGA_HALF)    // Blue
            5 -> Color(VGA_HALF, VGA_NONE, VGA_HALF)    // Magenta
            6 -> Color(VGA_NONE, VGA_HALF, VGA_HALF)    // Cyan
            7 -> Color(VGA_HALF, VGA_HALF, VGA_HALF)    // White
            8 -> Color(VGA_LOW, VGA_LOW, VGA_LOW)       // Bright black
            9 -> Color(VGA_FULL, VGA_LOW, VGA_LOW)      // Bright red
            10 -> Color(VGA_LOW, VGA_FULL, VGA_LOW)     // Bright green
            11 -> Color(VGA_FULL, VGA_FULL, VGA_LOW)    // Bright yellow
            12 -> Color(VGA_LOW, VGA_LOW, VGA_FULL)     // Bright blue
            13 -> Color(VGA_FULL, VGA_LOW, VGA_FULL)    // Bright magenta
            14 -> Color(VGA_LOW, VGA_FULL, VGA_FULL)    // Bright cyan
            15 -> Color(VGA_FULL, VGA_FULL, VGA_FULL)   // Bright white
            else -> throw IllegalArgumentException("Color number not in $BEGIN-$END range.")
        }
    }
}