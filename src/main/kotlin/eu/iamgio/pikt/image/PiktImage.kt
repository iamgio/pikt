package eu.iamgio.pikt.image

import eu.iamgio.pikt.properties.ColorsProperties
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.system.exitProcess

/**
 * Represent an image source.
 *
 * @param image AWT image
 * @param colors color scheme
 * @author Giorgio Garofalo
 */
class PiktImage(private val image: BufferedImage, private val colors: ColorsProperties) {

    /**
     * @param file image file
     * @param colors color scheme
     */
    constructor(file: File, colors: ColorsProperties) : this(readImage(file), colors)

    /**
     * Instantiates a [Pixel] instance from image coordinates.
     * @return pixel with color information
     */
    private fun getPixel(image: BufferedImage, x: Int, y: Int): Pixel {
        val rgb = image.getRGB(x, y)
        return Pixel(Color(rgb), colors)
    }

    /**
     * Generates an array of pixels out of the image.
     * @return collection of pixels
     */
    private fun generatePixelArray(): PixelArray {
        val pixels = Array(image.width * image.height) {
            val x: Int = it % image.width
            val y: Int = it / image.width
            getPixel(image, x, y)
        }
        return PixelArray(pixels)
    }

    /**
     * @return pixel-by-pixel reader for the image
     */
    fun reader(): PixelReader = PixelReader(generatePixelArray(), colors)

    private companion object {

        /**
         * Reads image from [file]. Exits if an error occurs.
         * @param file image file
         * @return [BufferedImage] loaded from [file]
         */
        private fun readImage(file: File): BufferedImage {
            return try {
                ImageIO.read(file)
            } catch(e: IOException) {
                System.err.println("Could not read image $file.\nExiting.")
                exitProcess(-1)
            }
        }
    }
}