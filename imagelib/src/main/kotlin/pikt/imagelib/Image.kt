package pikt.imagelib

import pikt.stdlib.newFile
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Abstraction of images for Pikt.
 *
 * @param image Java image
 */
class Image(private val image: BufferedImage) {
    constructor(file: File) : this(ImageIO.read(file))
    constructor(path: String) : this(File(path))

    /**
     * Width of the image.
     */
    val width: Int
        get() = image.width

    /**
     * Height of the image
     */
    val height: Int
        get() = image.height
}

/**
 * Instantiates a new writable [Image].
 * @param width image width as [Int]
 * @param height image height as [Int]
 * @return readable and writable image
 */
fun newImage(width: Any, height: Any): Image {
    if(width !is Int) {
        throw RuntimeException("Image width should be an integer, ${width.javaClass} found.")
    }
    if(height !is Int) {
        throw RuntimeException("Image height should be an integer, ${width.javaClass} found.")
    }
    return Image(BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB))
}

/**
 * Instantiates a writable [Image] from an image on disk.
 * @param pathOrFile either a [File] or a [String] path
 */
fun newImage(pathOrFile: Any) = Image(newFile(pathOrFile, requireExistance = true))

/**
 * @return width of [image]
 */
fun imageWidth(image: Any): Int {
    if(image !is Image) {
        throw RuntimeException("imageWidth: expected an image, ${image.javaClass} found.")
    }
    return image.width
}

/**
 * @return height of [image]
 */
fun imageHeight(image: Any): Int {
    if(image !is Image) {
        throw RuntimeException("imageWidth: expected an image, ${image.javaClass} found.")
    }
    return image.height
}