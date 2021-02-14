package eu.iamgio.pikt.statement

import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties

/**
 * Represents a statement or instruction
 *
 * @author Giorgio Garofalo
 */
abstract class Statement {

    /**
     * @return whether the pixel matches the statement's color
     */
    fun matches(pixel: Pixel, colors: ColorsProperties): Boolean = pixel.hex.equals(getHex(colors), ignoreCase = true)

    /**
     * @return statement's color from color schemes
     */
    abstract fun getHex(colors: ColorsProperties): String

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
     * Gets a statement matching the color of [pixel] based on colors scheme.
     * @return statement by pixel color if exists. <tt>null</tt> otherwise
     */
    fun getStatement(pixel: Pixel, colors: ColorsProperties): Statement? = statements.firstOrNull { it.matches(pixel, colors) }
}