package eu.iamgio.pikt.explain.image

import eu.iamgio.pikt.explain.data.ImageSpecsData
import java.awt.Graphics2D
import java.awt.image.BufferedImage

// This will be removed and automatically generated depending on code length.
private const val IMAGE_WIDTH = 500

/**
 * The final image that contains human-readable explanation of a Pikt source.
 *
 * @param sourceImage Pikt source image, already scaled
 * @param codeLines lines of human-readable explanation code
 * @param imageSpecs style of the image
 * @author Giorgio Garofalo
 */
class ExplanationImage(
        private val sourceImage: SourceImage,
        private val codeLines: List<String>,
        private val imageSpecs: ImageSpecsData
) {

    // Width and height of the output image, set during the generation.
    private var width = 0
    private var height = 0

    /**
     * Graphics of the image being generated.
     */
    private lateinit var graphics: Graphics2D

    /**
     * Fills the image with [ImageSpecsData.backgroundColor].
     */
    private fun drawBackground() {
        this.graphics.background = this.imageSpecs.backgroundColor
        this.graphics.clearRect(0, 0, this.width, this.height)
    }

    /**
     * Drawing lines with a spacing of [ImageSpecsData.lineHeight].
     */
    private fun drawSeparatorLines() {
        this.graphics.color = this.imageSpecs.separatorColor
        for(y in this.imageSpecs.lineHeight until this.height step this.imageSpecs.lineHeight) {
            this.graphics.fillRect(
                    0, y - this.imageSpecs.separatorSize / 2,
                    this.width, this.imageSpecs.separatorSize
            )
        }
    }

    /**
     * Draws the [sourceImage].
     */
    private fun drawSourceImage() {
        this.graphics.drawImage(this.sourceImage.image, 0, 0, null)
    }

    /**
     * Generates the explanation image.
     * @return the output image that contains the [sourceImage]
     *  and its human-readable explanation.
     */
    fun generate(): BufferedImage {
        val image = BufferedImage(IMAGE_WIDTH, this.sourceImage.height, BufferedImage.TYPE_INT_RGB)
        this.width = image.width
        this.height = image.height
        this.graphics = image.createGraphics()

        this.drawBackground()
        this.drawSeparatorLines()
        this.drawSourceImage()

        return image
    }
}