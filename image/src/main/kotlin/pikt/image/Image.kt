package pikt.image

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Abstraction of images for Pikt
 */
class Image(private val image: BufferedImage) {
    constructor(file: File) : this(ImageIO.read(file))
    constructor(path: String) : this(File(path))
}

/**
 * Instantiates a new writable [Image].
 * @param width image width as [Int]
 * @param height image height as [Int]
 * @return readable and writable image
 */
fun createImage(width: Any, height: Any): Image {
    if(width !is Int) {
        throw RuntimeException("Image width should be an integer, ${width::class.java} found.")
    }
    if(height !is Int) {
        throw RuntimeException("Image height should be an integer, ${width::class.java} found.")
    }
    return Image(BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB))
}