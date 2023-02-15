package eu.iamgio.pikt.explain.image

import eu.iamgio.pikt.explain.data.ImageSpecsData
import eu.iamgio.pikt.explain.image.layers.BackgroundLayer
import eu.iamgio.pikt.explain.image.layers.CodeLayer
import eu.iamgio.pikt.explain.image.layers.SeparatorLinesLayer
import eu.iamgio.pikt.explain.image.layers.SourceImageLayer
import eu.iamgio.pikt.explain.syntax.SyntaxHighlighting
import java.awt.Font
import java.awt.Graphics2D
import java.awt.GraphicsEnvironment
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File

/**
 * The final image that contains human-readable explanation of a Pikt source.
 *
 * @param sourceImage Pikt source image, already scaled
 * @param codeLines lines of human-readable explanation code
 * @param syntaxHighlighting syntax highlighting rules
 * @param imageSpecs style of the image
 * @author Giorgio Garofalo
 */
class ExplanationImage(
        private val sourceImage: SourceImage,
        private val codeLines: List<String>,
        private val syntaxHighlighting: SyntaxHighlighting,
        private val imageSpecs: ImageSpecsData
) {

    /**
     * Graphics of the image being generated.
     */
    private lateinit var graphics: Graphics2D

    /**
     * Text font.
     */
    private val font: Font = this.createFont()

    /**
     * X coordinate of code text.
     */
    private val codeX: Int
        get() = this.sourceImage.width + this.imageSpecs.lineHeight / 2

    /**
     * Layers to draw.
     */
    private val layers = listOf(
        BackgroundLayer(),
        SeparatorLinesLayer(),
        SourceImageLayer(this.sourceImage),
        CodeLayer(this.codeLines, this.syntaxHighlighting, this.font, this.codeX)
    )

    /**
     * Creates the font used for text.
     * If the [ImageSpecsData.fontFamily] refers to a file, the font is loaded from that file.
     * @return the text font
     */
    private fun createFont(): Font {
        // Check if the font family refers to a file.
        val fontFile = File(this.imageSpecs.fontFamily)

        val fontFamily = if(fontFile.exists()) {
            // If the font family refers to a file, register it.
            Font.createFont(Font.TRUETYPE_FONT, fontFile).also {
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(it)
            }.family
        } else {
            this.imageSpecs.fontFamily
        }

        return Font(fontFamily, Font.PLAIN, this.imageSpecs.fontSize)
    }

    /**
     * Calculates the width of the image based on its content, or fixed if [ImageSpecsData.width] is set.
     * @param font text font
     * @return image width
     */
    private fun calcImageWidth(font: Font): Int {
        return this.imageSpecs.width
            ?: (this.codeX + (this.codeLines.maxByOrNull { it.length }?.length?.times(font.size) ?: 0))
    }

    /**
     * Generates the explanation image.
     * @return the output image that contains the [sourceImage]
     *  and its human-readable explanation.
     */
    fun generate(): BufferedImage {
        val font = this.createFont()
        val image = BufferedImage(this.calcImageWidth(font), this.sourceImage.height, BufferedImage.TYPE_INT_RGB)
        this.graphics = image.createGraphics()
        this.graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB)

        this.layers.forEach {
            it.draw(this.graphics, this.imageSpecs, image.width, image.height)
        }

        return image
    }
}