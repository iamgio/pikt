package eu.iamgio.pikt.explain.image

import java.awt.image.BufferedImage

/**
 * A Pikt source.
 *
 * @param image image data
 * @author Giorgio Garofalo
 */
class SourceImage(val image: BufferedImage) {

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

    /**
     * Scales the image by a factor.
     * @param scaling scale technique to use
     * @param factor factor to scale the image by
     * @return a scaled copy of the source image
     */
    fun scale(scaling: ImageScaling, factor: Int): SourceImage {
        return SourceImage(scaling.scale(this.image, factor))
    }
}