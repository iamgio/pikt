package eu.iamgio.pikt.image

import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.properties.PiktProperties
import java.awt.image.BufferedImage
import java.io.File

/**
 * Represent an image source.
 *
 * @param image AWT image
 * @param colors color scheme
 * @author Giorgio Garofalo
 */
class PiktImage(private val image: BufferedImage, val colors: ColorsProperties) {

    /**
     * Used for compacting/decompacting copies of this image.
     */
    val compacter = ImageCompacter(this)

    /**
     * @param file image file
     * @param colors color scheme
     */
    constructor(file: File, colors: ColorsProperties) : this(readImage(file), colors)

    /**
     * @param properties Pikt properties
     */
    constructor(properties: PiktProperties) : this(properties.source, properties.colors)

    /**
     * Instantiates a [Pixel] instance from image coordinates.
     * @return pixel with color information
     */
    private fun getPixel(x: Int, y: Int): Pixel {
        val rgb = image.getRGB(x, y)
        return Pixel(Color(rgb), x, y, colors)
    }

    /**
     * Generates an array of pixels out of the image skipping whitespaces.
     * @return collection of pixels
     */
    private fun generatePixelArray(): PixelArray {
        val pixels = Array(image.width * image.height) {
            val x: Int = it % image.width
            val y: Int = it / image.width
            getPixel(x, y)
        }
        return PixelArray(pixels.filter { !it.isWhitespace })
    }

    /**
     * @return a new pixel-by-pixel reader for the image
     */
    fun reader(): PixelReader = PixelReader(generatePixelArray())
}