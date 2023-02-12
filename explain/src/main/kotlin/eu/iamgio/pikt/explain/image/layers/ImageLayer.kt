package eu.iamgio.pikt.explain.image.layers

import eu.iamgio.pikt.explain.data.ImageSpecsData
import java.awt.Graphics2D

/**
 * Represents a layer of the output image that contains some visual content.
 *
 * @author Giorgio Garofalo
 */
interface ImageLayer {

    /**
     * Draws the content on the image.
     * @param graphics image graphics
     * @param imageSpecs image style data
     * @param imageWidth width of the image
     * @param imageHeight height of the image
     */
    fun draw(graphics: Graphics2D, imageSpecs: ImageSpecsData, imageWidth: Int, imageHeight: Int)
}