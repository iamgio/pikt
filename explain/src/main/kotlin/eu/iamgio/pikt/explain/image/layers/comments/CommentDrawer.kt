package eu.iamgio.pikt.explain.image.layers.comments

import eu.iamgio.pikt.explain.data.CommentData
import eu.iamgio.pikt.explain.data.ImageSpecsData
import java.awt.Graphics2D

/**
 * Responsible for drawing [CommentData] on an image.
 *
 * @param comment comment to draw
 * @param T comment type
 * @author Giorgio Garofalo
 */
abstract class CommentDrawer<T : CommentData<*>>(protected val comment: T) {

    /**
     * Draws the content on the image.
     * @param graphics image graphics
     * @param imageSpecs image style data
     * @param imageX X coordinate of the source image
     */
    abstract fun draw(graphics: Graphics2D, imageSpecs: ImageSpecsData, imageX: Int)
}