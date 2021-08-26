package eu.iamgio.pikt.schemes

import eu.iamgio.pikt.properties.COLORS_SEPARATOR
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import java.util.*
import javax.imageio.ImageIO

/**
 * Width of the generated image.
 */
private const val IMAGE_WIDTH = 500

/**
 * Height for each property of the scheme.
 */
private const val PROPERTY_HEIGHT = 60

/**
 * Diameter of the color circle.
 */
private const val CIRCLE_DIAMETER = 20

/**
 * Space between the circle(s) and the text.
 */
private const val TEXT_MARGIN = 30

/**
 * Spacing between colors of the same property.
 */
private const val HORIZONTAL_SPACING = 10

/**
 * Represents a color palette generated by -exportscheme=name.
 *
 * @param schemeInputStream input stream of the color scheme (.properties).
 * @author Giorgio Garofalo
 */
class ColorSchemePalette(schemeInputStream: InputStream) {

    /**
     * Scheme as properties.
     */
    private val properties = Properties().also { it.load(schemeInputStream) }

    /**
     * Generates an image from the given scheme.
     * @param imageFile output image
     */
    fun generate(imageFile: File) {
        // Create image.
        val image = BufferedImage(IMAGE_WIDTH, (properties.size * PROPERTY_HEIGHT / 3.9).toInt(), BufferedImage.TYPE_INT_RGB)

        // Get graphics.
        val graphics = image.createGraphics()

        // Set antialiasing.
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // Set dark background.
        graphics.background = Color(40, 40, 40)

        // Sort properties keys. 'whitespace' goes first, stdlib members go last.
        val keys = properties.keys.sortedBy { key ->
            key.toString().let {
                when {
                    it.startsWith("stdlib") -> "|$it"
                    it == "whitespace" -> "_$it"
                    else -> it
                }
            }
        }

        // Add first half of the properties to the left.
        generateList(keys.subList(0, properties.size / 2 - 1), graphics, 15)

        // Add second half of the properties to the right.
        generateList(keys.subList(properties.size / 2, properties.size - 1), graphics, IMAGE_WIDTH / 2 + 15)

        // Save image.
        ImageIO.write(image, "png", imageFile)
    }

    /**
     * Generates a color-name list from properties.
     * @param keys properties keys
     * @param graphics image graphics
     * @param xOffset x coordinate where elements start at
     */
    private fun generateList(keys: List<Any>, graphics: Graphics2D, xOffset: Int) {
        // Y coordinate to place items at.
        var y = 0

        // Iterate properties
        keys.forEach { name ->
            val colors = properties.getValue(name).toString().split(COLORS_SEPARATOR)
            colors.forEachIndexed { colorIndex, hex ->
                try {
                    graphics.color = Color.decode("#$hex") // Does not go further if the color is invalid.
                    y += PROPERTY_HEIGHT / 2
                    graphics.fillOval(xOffset + colorIndex * HORIZONTAL_SPACING, y - CIRCLE_DIAMETER / 2, CIRCLE_DIAMETER, CIRCLE_DIAMETER)
                    if(colorIndex == 0) graphics.drawString(name.toString(), xOffset + TEXT_MARGIN + colors.size * HORIZONTAL_SPACING, y + CIRCLE_DIAMETER / 4)
                } catch(ignored: NumberFormatException) {}
            }
        }
    }
}