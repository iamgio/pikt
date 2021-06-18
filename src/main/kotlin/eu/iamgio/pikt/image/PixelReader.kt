package eu.iamgio.pikt.image

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.ExpressionParser
import eu.iamgio.pikt.expression.ExpressionType
import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Pixel-by-pixel reader of a [PiktImage]
 *
 * @param pixels collection of [Pixel]s
 * @param colors color scheme
 * @param statement statement of this reader, if already subdivided
 * @author Giorgio Garofalo
 */
class PixelReader(private val pixels: PixelArray, val colors: ColorsProperties, val statement: Statement? = null) {

    /**
     * Current pixel index.
     */
    var index: Int = -1

    /**
     * Amount of pixels.
     */
    val size: Int
        get() = pixels.size

    /**
     * Whether this reader has come into an error.
     * @see error
     */
    var isInvalidated: Boolean = false
        private set

    /**
     * Gets the next pixel available.
     * @return next pixel, `null` if there is none
     */
    fun next(): Pixel? {
        index++
        if(index >= pixels.size) return null

        return pixels[index]
    }

    /**
     * Executes a task for every non-null pixel
     * @param task task to be executed if the current pixel is not `null`.
     */
    fun whileNotNull(task: (Pixel) -> Unit) {
        var pixel: Pixel? = null
        while(next()?.also { pixel = it } != null) {
            task(pixel!!)
        }
    }

    /**
     * Creates a copy of this reader sliced from [start] to [end].
     * @return sliced copy of this reader
     */
    fun sliced(start: Int, end: Int, statement: Statement? = null) = PixelReader(pixels.sliced(start, end), colors, statement)

    /**
     * Subdivides this reader into one minor reader for each statement.
     * @return list of minor readers.
     */
    fun subdivide(): List<PixelReader> {
        val readers = mutableListOf<PixelReader>()

        var startIndex = 0

        while(true) {
            val pixel = next()
            if(startIndex != index && (pixel == null || pixel.isStatement)) {
                readers += sliced(startIndex, index - 1, pixels[startIndex].statement)
                startIndex = index
            }
            if(pixel == null) return readers
        }
    }

    /**
     * Reads the next expression as Kotlin code, be it a string, a number, a boolean or an object.
     * @return following value
     */
    fun nextExpression(type: ExpressionType? = null): Expression = ExpressionParser(this).eval(type)

    /**
     * Prints an error preceded by a standard prefix and invalidates this reader.
     * `Error at [x; y] (index i in Statement): message`
     * @param message message to log
     * @param syntax optional [statement]'s syntax that should be printed out
     */
    fun error(message: String, syntax: StatementSyntax? = null) {
        isInvalidated = true

        val coordinates = if(pixels.size > 0) {
            val pixel = pixels[if(pixels.size == 1) 0 else index - 1]
            " at (${pixel.x},${pixel.y})"
        } else ""
        System.err.println("Error$coordinates (index $index in ${statement?.name ?: "<no statement>"}):")
        System.err.println("\t$message")
        if(syntax != null) {
            val prefix = "Syntax: "
            System.err.println("\t$prefix" + syntax)
            System.err.println("\t${" ".repeat(prefix.length)}" + syntax.marksLine)
        }
        System.err.println()
    }
}