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
 * @param file image file
 * @author Giorgio Garofalo
 */
class PiktImage(private val file: File) {

    /**
     * Reads image from [file]. Exits if an error occurs.
     * @return [BufferedImage] loaded from [file]
     */
    private fun readImage(): BufferedImage {
        return try {
            ImageIO.read(file)
        } catch(e: IOException) {
            System.err.println("Could not read image $file.\nExiting.")
            exitProcess(-1)
        }
    }

    /**
     * Instantiates a [Pixel] instance from image coordinates.
     * @return pixel with color information
     */
    private fun getPixel(image: BufferedImage, x: Int, y: Int): Pixel {
        val rgb = image.getRGB(x, y)
        return Pixel(Color(rgb))
    }

    /**
     * Generates an array of pixels out of the image.
     * @return collection of pixels
     */
    private fun generatePixelArray(): PixelArray {
        val image = readImage()
        val pixels = Array(image.width * image.height) {
            val x: Int = it % image.height
            val y: Int = it / image.height
            getPixel(image, x, y)
        }
        return PixelArray(pixels)
    }

    /**
     * @return pixel-by-pixel reader for the image
     */
    fun reader(colors: ColorsProperties): PixelReader = PixelReader(generatePixelArray(), colors)
}