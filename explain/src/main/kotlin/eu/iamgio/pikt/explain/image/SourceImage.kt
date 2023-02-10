package eu.iamgio.pikt.explain.image

import java.awt.image.BufferedImage

/**
 * A Pikt source.
 *
 * @param image image data
 * @author Giorgio Garofalo
 */
class SourceImage(private val image: BufferedImage) {

    /**
     * Image width.
     */
    val width: Int
        get() = image.width

    /**
     * Image height.
     */
    val height: Int
        get() = image.height
}