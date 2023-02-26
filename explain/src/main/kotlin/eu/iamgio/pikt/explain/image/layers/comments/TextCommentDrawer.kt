package eu.iamgio.pikt.explain.image.layers.comments

import eu.iamgio.pikt.explain.data.ImageSpecsData
import eu.iamgio.pikt.explain.data.TextCommentData
import eu.iamgio.pikt.explain.image.calcImageCenteredTextX
import eu.iamgio.pikt.explain.image.calcImageCenteredTextY
import java.awt.Graphics2D

/**
 * A drawer for text comments.
 *
 * @author Giorgio Garofalo
 */
class TextCommentDrawer(comment: TextCommentData) : CommentDrawer<TextCommentData>(comment) {

    override fun draw(graphics: Graphics2D, imageSpecs: ImageSpecsData, imageX: Int) {
        val textWidth = graphics.fontMetrics.stringWidth(comment.content)

        graphics.drawString(
            comment.content,
            imageX + imageSpecs.calcImageCenteredTextX(comment.x, textWidth),
            imageSpecs.calcImageCenteredTextY(comment.y)
        )
    }
}