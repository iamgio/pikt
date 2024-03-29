package eu.iamgio.pikt.statement

import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.properties.ColorsProperty
import eu.iamgio.pikt.statement.statements.LambdaOpenStatement

/**
 * Represents a statement or instruction.
 *
 * @author Giorgio Garofalo
 */
abstract class Statement {

    /**
     * Defines whether this statement requires empty lines in the decompacted output image.
     * @see eu.iamgio.pikt.command.commands.imageprocessing.DecompactCommand
     * @see eu.iamgio.pikt.image.processing.ImageCompacter
     */
    open val decompactionStyle: DecompactionStyle = DecompactionStyle.NO_SPACING

    /**
     * Options of this statement, such as scope open/close behavior.
     */
    open val options: StatementOptions = StatementOptions()

    /**
     * Name of this statement.
     */
    val name: String
        get() = javaClass.statementName

    /**
     * Whether this statement opens a code block.
     */
    val isBlock: Boolean
        get() = this is LambdaOpenStatement

    /**
     * This statement cast to a lambda.
     */
    val asBlock: LambdaOpenStatement
        get() = this as LambdaOpenStatement

    /**
     * @return whether the pixel matches the statement's color
     */
    fun matches(pixel: Pixel): Boolean = pixel.matches(getColors(pixel.colors))

    /**
     * Statement syntax according to these rules:
     * - `<abc>` refers to an obligatory pixel;
     * - `<abc?>` refers to an optional pixel;
     * - `%abc%` refers to a color value from the color scheme;
     * - `...abc` refers to a sequential list of pixels.
     */
    abstract fun getSyntax(): StatementSyntax

    /**
     * @return statement's color from color schemes
     */
    abstract fun getColors(colors: ColorsProperties): ColorsProperty

    /**
     * Generates output code.
     * @param reader pixel reader
     * @param syntax syntax instance, so that calling [StatementSyntax.mark] applies syntax marks
     * @param data information about this generation
     * @return output code
     */
    abstract fun generate(reader: PixelReader, syntax: StatementSyntax, data: StatementData): CharSequence?

    /**
     * This method is called upon evaluation and defines the statement the evaluator will use to generate code.
     * It defaults to `this` for a GC-friendly approach.
     * However, if the statement has specific options that others may gain access to,
     * this has to be overridden in order to return a new instance of that statement.
     * @return evaluated statement
     */
    open fun getEvaluableInstance(): Statement = this

    /**
     * Different options for decompaction.
     *
     * @param hasEmptyLineBefore whether this statement should be preceeded by an empty line
     * @param hasEmptyLineAfter whether this statement should be followed by an empty line
     * @see eu.iamgio.pikt.command.commands.imageprocessing.DecompactCommand
     * @see eu.iamgio.pikt.image.processing.ImageCompacter
     */
    enum class DecompactionStyle(val hasEmptyLineBefore: Boolean, val hasEmptyLineAfter: Boolean) {
        NO_SPACING(false, false),
        SPACE_BEFORE(true, false),
        SPACE_AFTER(false, true),
        SPACE_BEFORE_AND_AFTER(true, true)
    }
}

/**
 * Name of a statement, given its class.
 */
val Class<out Statement>.statementName
    get() = simpleName.removeSuffix("Statement")
