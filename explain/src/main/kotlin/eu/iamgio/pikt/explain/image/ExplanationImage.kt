package eu.iamgio.pikt.explain.image

import eu.iamgio.pikt.explain.data.ImageSpecsData
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage

/**
 * Value to add to the Y coordinate of each line of code to have aligned text.
 */
private const val TEXT_Y_OFFSET = 5

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
     * X coordinate of code text.
     */
    private val codeX: Int
        get() = this.sourceImage.width + this.imageSpecs.lineHeight / 2

    /**
     * Creates the font used for text.
     * @return the text font
     */
    private fun createFont(): Font {
        return Font(this.imageSpecs.fontFamily, Font.PLAIN, this.imageSpecs.fontSize)
    }

    /**
     * Calculates the width of the image based on its content.
     * @param font text font
     * @return image width
     */
    private fun calcImageWidth(font: Font): Int {
        return this.codeX + (this.codeLines.maxByOrNull { it.length }?.length?.times(font.size) ?: 0)
    }

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
     * Draws the content of [codeLines].
     * @param font text font
     */
    private fun drawCode(font: Font) {
        this.graphics.font = font
        this.graphics.color = this.imageSpecs.textColor

        this.codeLines.forEachIndexed { index, line ->
            this.graphics.drawString(
                    line,
                    this.codeX,
                    index * this.imageSpecs.lineHeight + this.imageSpecs.lineHeight / 2 + TEXT_Y_OFFSET
            )
        }
    }

    /**
     * Generates the explanation image.
     * @return the output image that contains the [sourceImage]
     *  and its human-readable explanation.
     */
    fun generate(): BufferedImage {
        val font = this.createFont()
        val image = BufferedImage(this.calcImageWidth(font), this.sourceImage.height, BufferedImage.TYPE_INT_RGB)
        this.width = image.width
        this.height = image.height
        this.graphics = image.createGraphics()
        this.graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB)

        this.drawBackground()
        this.drawSeparatorLines()
        this.drawSourceImage()
        this.drawCode(font)

        return image
    }
}