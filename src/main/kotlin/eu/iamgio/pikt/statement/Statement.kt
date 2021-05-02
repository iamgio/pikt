package eu.iamgio.pikt.statement

import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.properties.ColorsProperty

/**
 * Represents a statement or instruction
 *
 * @author Giorgio Garofalo
 */
abstract class Statement {

    /**
     * @return whether the pixel matches the statement's color
     */
    fun matches(pixel: Pixel): Boolean = pixel.matches(getColors(pixel.colors))

    /**
     * @return statement's color from color schemes
     */
    abstract fun getColors(colors: ColorsProperties): ColorsProperty

    /**
     * Generates Kotlin code.
     * @param reader pixel reader
     * @return Kotlin code
     */
    abstract fun generate(reader: PixelReader): String
}

/**
 * Single-instance class that contains statements information.
 *
 * @author Giorgio Garofalo
 */
object Statements {

    private val statements = mutableListOf<Statement>()

    /**
     * Registers a new statement.
     * @param statement statement to be registered
     */
    fun register(statement: Statement) {
        statements += statement
    }

    /**
     * Gets a statement matching the color of [pixel] based on color scheme.
     * @return statement by pixel color if exists. <tt>null</tt> otherwise
     */
    fun byPixel(pixel: Pixel): Statement? = statements.firstOrNull { it.matches(pixel) }
}