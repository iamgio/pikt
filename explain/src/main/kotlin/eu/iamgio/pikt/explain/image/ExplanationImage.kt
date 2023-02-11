package eu.iamgio.pikt.explain.image

import eu.iamgio.pikt.explain.data.ImageSpecsData
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
    /**
     * The image being generated.
     */
    private lateinit var image: BufferedImage

    /**
     * Generates the explanation image.
     * @return the output image that contains the [sourceImage]
     *  and its human-readable explanation.
     */
    fun generate(): BufferedImage {
        this.image = BufferedImage(IMAGE_WIDTH, this.sourceImage.height, BufferedImage.TYPE_INT_RGB)
        return image
    }
}