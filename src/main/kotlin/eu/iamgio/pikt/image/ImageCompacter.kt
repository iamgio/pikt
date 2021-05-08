package eu.iamgio.pikt.image

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * @author Giorgio Garofalo
 */
class ImageCompacter(private val piktImage: PiktImage) {

    /**
     * @param size image size, excluding whitespaces
     * @param width expected image width. Will be calculated if <tt>null</tt>
     * @param height expected image height. Will be calculated if <tt>null</tt>
     * @return width and height of the compacted image
     */
    private fun calcCompactSize(size: Int, width: Int?, height: Int?): Pair<Int, Int> {
        val imageWidth: Int
        val imageHeight: Int
        if(width == null && height == null) {
            // None are specified
            imageWidth = sqrt(size.toDouble()).toInt()
            imageHeight = ceil(size / imageWidth.toDouble()).toInt()
        } else if(width != null && height == null) {
            // Width is specified
            imageWidth = width
            imageHeight = ceil(size / width.toDouble()).toInt()
        } else if(width == null && height != null) {
            // Height is specified
            imageHeight = height
            imageWidth = ceil(size / imageHeight.toDouble()).toInt()
        } else {
            // Both are specified
            imageWidth = width!!
            imageHeight = height!!
        }
        return imageWidth to imageHeight
    }

    /**
     * Creates a copy of the image with no whitespaces.
     * @param width expected image width. Will be calculated if <tt>null</tt>
     * @param height expected image height. Will be calculated if <tt>null</tt>
     * @return compacted copy of the source image
     */
    fun compact(width: Int?, height: Int?): BufferedImage {
        val reader = piktImage.reader()
        val imageWidth: Int
        val imageHeight: Int
        calcCompactSize(reader.size, width, height).let {
            imageWidth = it.first
            imageHeight = it.second
        }

        val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB)

        fun setRGB(index: Int, color: Color) = image.setRGB(index % imageWidth, index / imageWidth, color.rgb)

        // Copy PixelReader content.
        reader.whileNotNull {
            setRGB(reader.index, it.color)
        }

        // Fill the remaining space with whitespaces.
        val background = reader.colors.whitespace.colors.firstOrNull()?.let { Color.decode("#$it") } ?: Color.WHITE
        (reader.index until imageWidth * imageHeight).forEach {
            setRGB(it, background)
        }

        return image
    }
}