package eu.iamgio.pikt.image

import eu.iamgio.pikt.exit.ERROR_FAILED_IMAGE_PROCESSING
import eu.iamgio.pikt.exit.exit
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.statements.LambdaCloseStatement
import eu.iamgio.pikt.statement.statements.LambdaOpenStatement
import java.awt.image.BufferedImage
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * @author Giorgio Garofalo
 */
class ImageCompacter(private val piktImage: PiktImage) {

    private fun BufferedImage.applyBackground(colors: ColorsProperties, startIndex: Int = 0) {
        val background = colors.whitespace.colors.firstOrNull()?.let { Color.fromHex(it) } ?: Color.WHITE
        (startIndex until width * height).forEach {
            setRGB(it, background)
        }
    }

    /**
     * @param size image size, excluding whitespaces
     * @param width expected image width. Will be calculated if `null`
     * @param height expected image height. Will be calculated if `null`
     * @return width and height of the compacted image
     */
    private fun calcCompactSize(size: Int, width: Int?, height: Int?): Pair<Int, Int> {
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

    /**
     * Creates a copy of the image with no whitespaces.
     * @param width expected image width. Will be calculated if `null`
     * @param height expected image height. Will be calculated if `null`
     * @param mask if not `null`, defines the shape the pixels should fill
     * @return compacted copy of the source image
     */
    fun compact(width: Int?, height: Int?, mask: PixelMask? = null): BufferedImage {
        val reader = piktImage.reader()
        val (imageWidth, imageHeight) = calcCompactSize(reader.size, width, height)

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

    /**
     * Creates a copy of the image with a statement per line.
     * @return decompacted copy of the source image
     */
    fun decompact(): BufferedImage {
        val reader = piktImage.reader()
        val lines = mutableListOf<List<Int?>>() // Lines of RGB pixels. A line = a statement.

        var pixels = mutableListOf<Int?>() // Current line
        var currentDecompactionStyle = Statement.DecompactionStyle.NO_SPACING
        var currentShift = 0 // TAB-like spaces to distinguish sub-blocks (defined by lambdas).

        // Appends a line and checks if it requires spacing (via decompaction style).
        fun append(isLast: Boolean = false) {
            fun addLine(line: List<Int?>) {
                if(line.isNotEmpty() || lines.last().isNotEmpty()) lines.add(line)
            }

            // Add empty line before the statement if required.
            if(currentDecompactionStyle.hasEmptyLineBefore) addLine(emptyList())

            // Add current line.
            addLine(pixels)

            // Add empty line after the statement if required.
            if(currentDecompactionStyle.hasEmptyLineAfter && !isLast) addLine(emptyList())
        }

        // Changes the shift (amount of TABs) if there is a lambda statement and adds null pixels for each shift.
        fun applyShift(statement: Statement) {
            if(statement is LambdaCloseStatement) currentShift--

            repeat(currentShift) {
                pixels += null
            }

            if(statement is LambdaOpenStatement) currentShift++
        }

        // Reads the content and updates lines.
        reader.whileNotNull { pixel ->
            if(pixel.isStatement) {
                if(pixels.isNotEmpty()) {
                    append()
                }
                pixels = mutableListOf()
                applyShift(pixel.statement!!)

                currentDecompactionStyle = pixel.statement!!.decompactionStyle
            }
            pixels += pixel.color.rgb
        }
        if(pixels.isNotEmpty()) append(true)

        // Create image.
        val image = BufferedImage(lines.maxByOrNull { it.size }!!.size, lines.size, BufferedImage.TYPE_INT_RGB)
        image.applyBackground(piktImage.colors)

        // Append lines to the image.
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, rgb ->
                if(rgb != null) image.setRGB(x, y, rgb)
            }
        }

        return image
    }
}