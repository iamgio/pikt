package eu.iamgio.pikt.image

import eu.iamgio.pikt.properties.ColorsProperties
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * @author Giorgio Garofalo
 */
class ImageCompacter(private val piktImage: PiktImage) {

    private fun BufferedImage.setRGB(index: Int, color: Color) = setRGB(index % width, index / width, color.rgb)

    private fun BufferedImage.applyBackground(colors: ColorsProperties, startIndex: Int = 0) {
        val background = colors.whitespace.colors.firstOrNull()?.let { Color.decode("#$it") } ?: Color.WHITE
        (startIndex until width * height).forEach {
            setRGB(it, background)
        }
    }

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

        // Copy PixelReader content.
        reader.whileNotNull {
            image.setRGB(reader.index, it.color)
        }

        // Fill the remaining space with whitespaces.
        image.applyBackground(reader.colors, reader.index)

        return image
    }

    /**
     * Creates a copy of the image with a statement per line.
     * @return decompacted copy of the source image
     */
    fun decompact(): BufferedImage {
        val reader = piktImage.reader()
        val lines = mutableListOf<List<Int>>() // Lines of RGB pixels. A line = a statement.

        var pixels = mutableListOf<Int>() // Current line
        reader.whileNotNull {
            if(it.isStatement) {
                if(pixels.isNotEmpty()) lines += pixels
                pixels = mutableListOf()
            }
            pixels += it.color.rgb
        }
        if(pixels.isNotEmpty()) lines += pixels

        val image = BufferedImage(lines.maxByOrNull { it.size }!!.size, lines.size, BufferedImage.TYPE_INT_RGB)
        image.applyBackground(reader.colors)

        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, rgb ->
                image.setRGB(x, y, rgb)
            }
        }

        return image
    }
}