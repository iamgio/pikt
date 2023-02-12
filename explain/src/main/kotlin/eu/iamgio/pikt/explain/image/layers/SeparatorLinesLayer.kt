package eu.iamgio.pikt.explain.image.layers

import eu.iamgio.pikt.explain.data.ImageSpecsData
import java.awt.Graphics2D

/**
 * Layer of lines that separate lines of code.
 *
 * @author Giorgio Garofalo
 */
class SeparatorLinesLayer : ImageLayer {

    override fun draw(graphics: Graphics2D, imageSpecs: ImageSpecsData, imageWidth: Int, imageHeight: Int) {
        graphics.color = imageSpecs.separatorColor

        for(y in imageSpecs.lineHeight until imageHeight step imageSpecs.lineHeight) {
            graphics.fillRect(
                0, y - imageSpecs.separatorSize / 2,
                imageWidth, imageSpecs.separatorSize
            )
        }
    }
}