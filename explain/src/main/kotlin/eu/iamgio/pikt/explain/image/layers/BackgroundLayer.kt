package eu.iamgio.pikt.explain.image.layers

import eu.iamgio.pikt.explain.data.ImageSpecsData
import java.awt.Graphics2D

/**
 * Background layer.
 *
 * @author Giorgio Garofalo
 */
class BackgroundLayer : ImageLayer {

    override fun draw(graphics: Graphics2D, imageSpecs: ImageSpecsData, imageWidth: Int, imageHeight: Int) {
        graphics.background = imageSpecs.backgroundColor
        graphics.clearRect(0, 0, imageWidth, imageHeight)
    }
}