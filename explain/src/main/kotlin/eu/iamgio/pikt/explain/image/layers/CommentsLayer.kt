package eu.iamgio.pikt.explain.image.layers

import eu.iamgio.pikt.explain.data.CommentData
import eu.iamgio.pikt.explain.data.ImageSpecsData
import eu.iamgio.pikt.explain.image.calcImageCenteredTextX
import eu.iamgio.pikt.explain.image.calcImageCenteredTextY
import java.awt.Font
import java.awt.Graphics2D

/**
 * Layer with comments on the source image.
 *
 * @param comments comments to display
 * @param font font used
 * @param imageX X coordinate of the source image on the final image
 * @author Giorgio Garofalo
 */
class CommentsLayer(
    private val comments: List<CommentData>,
    private val font: Font,
    private val imageX: Int
) : ImageLayer {

    override fun draw(graphics: Graphics2D, imageSpecs: ImageSpecsData, imageWidth: Int, imageHeight: Int) {
        graphics.font = this.font
        graphics.color = imageSpecs.commentColor

        this.comments.forEach { comment ->
            val textWidth = graphics.fontMetrics.stringWidth(comment.text)
            graphics.drawString(
                comment.text,
                imageX + imageSpecs.calcImageCenteredTextX(comment.x, textWidth),
                imageSpecs.calcImageCenteredTextY(comment.y)
            )
        }
    }
}
