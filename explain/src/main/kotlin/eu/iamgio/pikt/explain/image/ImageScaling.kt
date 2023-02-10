package eu.iamgio.pikt.explain.image

import java.awt.image.BufferedImage

/**
 * Represents a techique used to scale an image.
 *
 * @author Giorgio Garofalo
 */
sealed interface ImageScaling {

    /**
     * Scales an image by a factor.
     * @param source image to scale
     * @param factor factor to scale the image by
     * @return a scaled copy of the source image
     */
    fun scale(source: BufferedImage, factor: Int): BufferedImage
}