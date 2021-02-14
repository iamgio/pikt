package eu.iamgio.pikt.image

import java.awt.Color

/**
 * Represents a single pixel of a [PiktImage]
 *
 * @param color AWT color of the pixel
 * @author Giorgio Garofalo
 */
data class Pixel(private val color: Color) {

    /**
     * @return whether the pixel is a whitespace (either white or non-opaque), hence should be skipped
     */
    fun isWhitespace(): Boolean = color.rgb == -1 || color.alpha != 255

    /**
     * @return [color] as hexadecimal
     */
    fun getHex(): String = Integer.toHexString(color.rgb).substring(2)
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
     * Gets a pixel from the array from given index.
     * @param index item index
     * @return pixel by index
     */
    operator fun get(index: Int): Pixel = pixels[index]

    override fun toString() = "PixelArray(size=${pixels.size})"
}