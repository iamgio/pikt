package eu.iamgio.pikt.explain.image.layers

import eu.iamgio.pikt.explain.data.ImageSpecsData
import java.awt.Font
import java.awt.Graphics2D

/**
 * Value to add to the Y coordinate of each line of code to have aligned text.
 */
private const val TEXT_Y_OFFSET = 5

/**
 * Layer with text of code.
 *
 * @param codeLines lines of code to display
 * @param font font used
 * @param x X coordinate of the text
 * @author Giorgio Garofalo
 */
class CodeLayer(private val codeLines: List<String>, private val font: Font, private val x: Int) : ImageLayer {

    override fun draw(graphics: Graphics2D, imageSpecs: ImageSpecsData, imageWidth: Int, imageHeight: Int) {
        graphics.font = this.font
        graphics.color = imageSpecs.textColor

        this.codeLines.forEachIndexed { index, line ->
            graphics.drawString(
                line,
                this.x,
                index * imageSpecs.lineHeight + imageSpecs.lineHeight / 2 + TEXT_Y_OFFSET
            )
        }
    }
}