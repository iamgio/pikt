package eu.iamgio.pikt.image

import eu.iamgio.pikt.util.ERROR_BAD_IO
import eu.iamgio.pikt.util.exit
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

// Utilities for working with (Buffered)Images

/**
 * Reads image from [file]. Exits if an error occurs.
 * @param file image file
 * @return [BufferedImage] loaded from [file]
 */
fun readImage(file: File): BufferedImage {
    return try {
        ImageIO.read(file)
    } catch(e: IOException) {
        exit(ERROR_BAD_IO, message = "Could not read image $file.")
    }
}

/**
 * Saves this image to [file]. Exits if an error occurs.
 * @param file image file
 */
fun BufferedImage.save(file: File) {
    try {
        ImageIO.write(this, "png", file)
    } catch(e: IOException) {
        exit(ERROR_BAD_IO, message = "Could not save image to $file.")
    }
}

/**
 * @return a copy of this image.
 */
fun BufferedImage.clone() = BufferedImage(colorModel, copyData(null), colorModel.isAlphaPremultiplied, null)

/**
 * Sets the value of a pixel in an image from a given pixel [index].
 */
fun BufferedImage.setRGB(index: Int, color: Color) = setRGB(index % width, index / width, color.rgb)

/**
 * Reads the image content pixel by pixel.
 * @param action action to run for each (x, y) pair
 */
fun BufferedImage.readLineByLine(action: (Int, Int) -> Unit) {
    (0 until height).forEach { y ->
        (0 until width).forEach { x ->
            action(x, y)
        }
    }
}