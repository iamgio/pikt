package eu.iamgio.pikt.explain.image.layers.comments

import eu.iamgio.pikt.explain.data.ImageSpecsData
import eu.iamgio.pikt.explain.data.LineCommentData
import eu.iamgio.pikt.explain.image.calcImagePixelCenter
import java.awt.BasicStroke
import java.awt.Graphics2D

/**
 * A drawer for line comments that connects two positions.
 *
 * @author Giorgio Garofalo
 */
class LineCommentDrawer(comment: LineCommentData) : CommentDrawer<LineCommentData>(comment) {

    override fun draw(graphics: Graphics2D, imageSpecs: ImageSpecsData, imageX: Int) {
        graphics.stroke = BasicStroke(2.5F)

        val (destinationX, destinationY) = comment.content

        graphics.drawLine(
            imageSpecs.calcImagePixelCenter(comment.x) + imageX, imageSpecs.calcImagePixelCenter(comment.y),
            imageSpecs.calcImagePixelCenter(destinationX) + imageX, imageSpecs.calcImagePixelCenter(destinationY)
        )
    }
}