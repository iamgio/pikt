package eu.iamgio.pikt.schemes

import eu.iamgio.pikt.image.rgbToHex
import eu.iamgio.pikt.properties.ColorsProperty
import eu.iamgio.pikt.properties.INTERNAL_COLORS_SCHEME_PATH
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.*

/**
 * Class that allows operations between an image, its custom scheme and the default internal scheme.
 * @param customScheme custom colors scheme to handle
 * @author Giorgio Garofalo
 */
sealed class ImageSchemeProcessing(private val image: BufferedImage, private val customScheme: Properties) {

    /**
     * @return the schemes as a pair of two maps: the internal one and the custom one
     */
    protected fun retrieveColors(): Pair<Map<String, ColorsProperty>, Map<String, ColorsProperty>> {
        fun Properties.asMap() = keys.associate { it.toString() to ColorsProperty.of(getProperty(it.toString())) }
        val internalScheme = Properties().also { it.load(javaClass.getResourceAsStream(INTERNAL_COLORS_SCHEME_PATH)!!) }
        return internalScheme.asMap() to customScheme.asMap()
    }

    /**
     * @return a copy of [image]
     */
    protected fun cloneImage() = BufferedImage(image.colorModel, image.copyData(null), image.colorModel.isAlphaPremultiplied, null)

    /**
     * Reads the image content pixel by pixel.
     * @param action action to run for each (x, y) pair
     */
    protected fun readImage(action: (Int, Int) -> Unit) {
        (0 until image.height).forEach { y ->
            (0 until image.width).forEach { x ->
                action(x, y)
            }
        }
    }

    /**
     * Runs the process on a copy of [image].
     */
    abstract fun process(): BufferedImage
}

class StandardizeImageProcessing(image: BufferedImage, customScheme: Properties) : ImageSchemeProcessing(image, customScheme) {

    override fun process(): BufferedImage {
        // Get data.
        val image = cloneImage()
        val internal: Map<String, ColorsProperty>
        val custom: Map<String, ColorsProperty>
        with(retrieveColors()) {
            internal = first
            custom = second
        }

        // Read image pixels
        readImage { x, y ->
            val hex = image.getRGB(x, y).rgbToHex()

            // Check for match between the pixel and a value from the custom scheme.
            custom.forEach { (key, color) ->
                if(color.has(hex)) {
                    // If found, replace with default color.
                    image.setRGB(x, y, Color.decode("#" + internal.getValue(key).colors.first()).rgb)
                    return@forEach
                }
            }
        }
        return image
    }
}