package eu.iamgio.pikt.image

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.system.exitProcess

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
        System.err.println("Could not read image $file.\nExiting.")
        exitProcess(-1)
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
        System.err.println("Could not save image to $file.\nExiting.")
        exitProcess(-1)
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