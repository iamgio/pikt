package eu.iamgio.pikt.explain.image.layers

import eu.iamgio.pikt.explain.data.ImageSpecsData
import eu.iamgio.pikt.explain.image.SourceImage
import java.awt.Graphics2D

/**
 * Layer of the source Pikt image.
 *
 * @param sourceImage Pikt source image
 * @author Giorgio Garofalo
 */
class SourceImageLayer(private val sourceImage: SourceImage) : ImageLayer {

    override fun draw(graphics: Graphics2D, imageSpecs: ImageSpecsData, imageWidth: Int, imageHeight: Int) {
        graphics.drawImage(sourceImage.image, 0, 0, null)
    }
}