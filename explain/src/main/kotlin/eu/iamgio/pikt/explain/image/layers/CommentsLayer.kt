package eu.iamgio.pikt.explain.image.layers

import eu.iamgio.pikt.explain.data.ImageSpecsData
import eu.iamgio.pikt.explain.data.LineCommentData
import eu.iamgio.pikt.explain.data.TextCommentData
import eu.iamgio.pikt.explain.image.layers.comments.LineCommentDrawer
import eu.iamgio.pikt.explain.image.layers.comments.TextCommentDrawer
import java.awt.Font
import java.awt.Graphics2D

/**
 * Layer with comments on the source image.
 *
 * @param textComments comments to display
 * @param font font used
 * @param imageX X coordinate of the source image on the final image
 * @author Giorgio Garofalo
 */
class CommentsLayer(
    private val textComments: List<TextCommentData>,
    private val lineComments: List<LineCommentData>,
    private val font: Font,
    private val imageX: Int
) : ImageLayer {

    override fun draw(graphics: Graphics2D, imageSpecs: ImageSpecsData, imageWidth: Int, imageHeight: Int) {
        graphics.font = this.font
        graphics.color = imageSpecs.commentColor

        val drawers = (this.textComments.map { TextCommentDrawer(it) }
                + this.lineComments.map { LineCommentDrawer(it) })

        drawers.forEach { it.draw(graphics, imageSpecs, imageX) }
    }
}
