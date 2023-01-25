package eu.iamgio.pikt.image.processing

import eu.iamgio.pikt.exit.ERROR_FAILED_IMAGE_PROCESSING
import eu.iamgio.pikt.exit.exit
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.image.PixelMask
import eu.iamgio.pikt.image.applyBackground
import eu.iamgio.pikt.image.setRGB
import java.awt.image.BufferedImage
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * Responsible for removing whitespace from an image.
 *
 * @param piktImage source image data
 * @param width expected image width. Will be calculated if `null`
 * @param height expected image height. Will be calculated if `null`
 * @param mask if not `null`, defines the shape the pixels should fill
 * @author Giorgio Garofalo
 */
class ImageCompacter(
    private val piktImage: PiktImage,
    private val width: Int?,
    private val height: Int?,
    private val mask: PixelMask? = null
) : ImageProcessing {

    /**
     * @param size image size, excluding whitespaces
     * @return width and height of the compacted image
     */
    private fun calcSize(size: Int): Pair<Int, Int> {
        val imageWidth: Int
        val imageHeight: Int

        when {
            // None are specified
            width == null && height == null -> {
                imageWidth = sqrt(size.toDouble()).toInt()
                imageHeight = ceil(size / imageWidth.toDouble()).toInt()
            }

            // Width is specified
            width != null && height == null -> {
                imageWidth = width
                imageHeight = ceil(size / imageWidth.toDouble()).toInt()
            }

            // Height is specified
            width == null && height != null -> {
                imageHeight = height
                imageWidth = ceil(size / imageHeight.toDouble()).toInt()
            }

            // Both are specified
            else -> {
                imageWidth = width!!
                imageHeight = height!!
            }
        }
        return imageWidth to imageHeight
    }

    override fun process(): BufferedImage {
        val reader = piktImage.reader()
        val (imageWidth, imageHeight) = this.calcSize(reader.size)

        if(reader.size > imageWidth * imageHeight) {
            exit(
                ERROR_FAILED_IMAGE_PROCESSING,
                message = "Error while compacting: given size is ${imageWidth * imageHeight} ($imageWidth*$imageHeight), but source has ${reader.size} elements."
            )
        }
        if(mask != null && reader.size != mask.size) {
            exit(
                ERROR_FAILED_IMAGE_PROCESSING,
                message = "Error while masking: mask has ${mask.size} elements, but source has ${reader.size}."
            )
        }

        val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB)

        // Fill the remaining space with whitespaces (when using a mask, otherwise it's done at the end to save time).
        if(mask != null) {
            image.applyBackground(piktImage.colors)
        }

        // Copy PixelReader content to the output image.
        reader.whileNotNull {
            if(mask == null) {
                image.setRGB(reader.index, it.color)
            } else {
                val maskComponent = mask.getComponentByIndex(reader.index) ?: return@whileNotNull
                image.setRGB(maskComponent.x, maskComponent.y, it.color.rgb)
            }
        }

        // Fill the remaining space with whitespaces (when not using a mask, otherwise it's done at the beginning).
        if(mask == null) {
            image.applyBackground(piktImage.colors, reader.index)
        }

        return image
    }
}