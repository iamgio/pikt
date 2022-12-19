package eu.iamgio.pikt.log.console.color

import com.diogonunes.jcolor.Attribute

/**
 * Represents a colored attribute of some text on the console.
 *
 * @author Giorgio Garofalo
 */
interface ConsoleAttributeColor {
    /**
     * @return this color as a background attribute
     */
    fun asBackground(): Attribute

    /**
     * @return this color as a text attribute
     */
    fun asText(): Attribute
}

/**
 * An attribute with R, G, B color components (`"TrueColor"`).
 *
 * @author Giorgio Garofalo
 */
class RGBConsoleAttributeColor(private val red: Int, private val green: Int, private val blue: Int) : ConsoleAttributeColor {
    override fun asBackground(): Attribute = Attribute.BACK_COLOR(red, green, blue)
    override fun asText(): Attribute = Attribute.TEXT_COLOR(red, green, blue)
}

/**
 * An attribute whose color is defined by a [colorNumber] of a fixed palette.
 *
 * @author Giorgio Garofalo
 */
class ColorNumberConsoleAttributeColor(private val colorNumber: Int) : ConsoleAttributeColor {
    override fun asBackground(): Attribute = Attribute.BACK_COLOR(colorNumber)
    override fun asText(): Attribute = Attribute.TEXT_COLOR(colorNumber)
}