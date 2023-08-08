package pikt.imagelib

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Abstraction of images for Pikt.
 *
 * @param image Java image
 */
class PiktImage(private val image: BufferedImage) {
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

    override fun toString() = "PiktImage (width=$width, height=$height)"
}