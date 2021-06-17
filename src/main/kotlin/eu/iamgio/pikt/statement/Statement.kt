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
     * Defines whether this statement requires empty lines in the decompacted output image.
     * @see eu.iamgio.pikt.command.commands.DecompactCommand
     * @see eu.iamgio.pikt.image.ImageCompacter
     */
    open val decompactionStyle: DecompactionStyle = DecompactionStyle.NO_SPACING

    /**
     * Statement previous to this statement, if exists.
     */
    var previousStatement: Statement? = null

    /**
     * Statement next to this statement, if exists.
     */
    var nextStatement: Statement? = null

    /**
     * Name of this statement
     */
    val name: String
        get() = javaClass.simpleName

    /**
     * @return whether the pixel matches the statement's color
     */
    fun matches(pixel: Pixel): Boolean = pixel.matches(getColors(pixel.colors))

    /**
     * Statement syntax according to these rules:
     * - `<abc>` refers to an obligatory pixel;
     * - `<abc?>` refers to an optional pixel;
     * - `%abc%` refers to a color value from the color scheme;
     * - ...abc refers to a sequential list of pixels.
     */
    abstract val syntax: String

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

    /**
     * Different options for decompaction.
     *
     * @see eu.iamgio.pikt.command.commands.DecompactCommand
     * @see eu.iamgio.pikt.image.ImageCompacter
     */
    enum class DecompactionStyle(val hasEmptyLineBefore: Boolean, val hasEmptyLineAfter: Boolean) {
        NO_SPACING(false, false),
        BEFORE(true, false),
        AFTER(false, true),
        BEFORE_AND_AFTER(true, true)
    }
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