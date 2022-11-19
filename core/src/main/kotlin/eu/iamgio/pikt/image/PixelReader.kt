package eu.iamgio.pikt.image

import eu.iamgio.pikt.eval.Scope
import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.ExpressionParser
import eu.iamgio.pikt.expression.ExpressionType
import eu.iamgio.pikt.expression.PixelSequence
import eu.iamgio.pikt.log.pixel.PixelLogger
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
     * @return a non-deep copy of this reader with its index set to `0`
     */
    fun softCopy(): PixelReader = PixelReader(pixels, colors, statement)

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
     * Reads the next pixels as a sequence.
     * A sequence is built by pixels separated by a dot operator.
     * @return next pixel sequence
     */
    fun nextSequence(): PixelSequence {
        val sequence = mutableListOf<Pixel>()
        while(true) {
            val pixel = next()
            if(pixel == null) {
                if(sequence.isNotEmpty()) error("Expected pixel after dot operator.")
                break
            }
            val next = next()
            sequence += pixel
            if(next == null) break
            if(!next.isDot) {
                index--
                break
            }
        }
        return PixelSequence(sequence.toTypedArray())
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
     * Executes a task for every non-empty pixel sequence
     * @param task task to be executed if the current sequence is not empty.
     */
    fun forEachNextSequence(task: (PixelSequence) -> Unit) {
        var sequence: PixelSequence
        while(!nextSequence().also { sequence = it }.isEmpty) {
            task(sequence)
        }
    }

    /**
     * Creates a copy of this reader sliced from [start] to [end].
     * @return sliced copy of this reader
     */
    fun sliced(start: Int, end: Int, statement: Statement? = null) = PixelReader(pixels.sliced(start, if(end < size) end else size - 1), colors, statement)

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
    fun nextExpression(scope: Scope, type: ExpressionType? = null): Expression = ExpressionParser(this, scope).eval(type)

    /**
     * Prints an error preceded by a standard prefix and invalidates this reader.
     * `Error at (x,y) (index i in Statement): message`
     * @param message message to log
     * @param syntax optional [statement]'s syntax that should be printed out
     * @param referenceToFirstPixel whether the error must reference the first pixel in the reader
     */
    fun error(message: String, syntax: StatementSyntax? = null, referenceToFirstPixel: Boolean = false) {
        isInvalidated = true

        // Index of the pixel that caused the error
        val pixelIndex = if(referenceToFirstPixel || index < 0) 0 else index - 1

        // Where the error happened.
        val coordinates = if(!pixels.isEmpty) {
            val pixel = pixels.getOrNull(pixelIndex) ?: pixels.last()
            " at (${pixel.x},${pixel.y})"
        } else ""

        System.err.println("Error$coordinates (index $pixelIndex in ${statement?.name ?: "<no statement>"}):")
        System.err.println("\t$message")

        // Logs the pixels of this reader with the selected logger.
        PixelLogger.currentLogger?.logReaderWithMark(this, markIndex = pixelIndex)

        // Prints a nice message that explains the expected syntax vs the used syntax.
        // Example from SetVariableStatement:
        //
        // Syntax: <%variable.set%> <name> <value>
        //	               ✓           ✓      ✗
        if(syntax != null) {
            val prefix = "Syntax: "
            System.err.println("\t" + prefix + syntax)
            System.err.println("\t" + " ".repeat(prefix.length) + syntax.marksLine)
        }
        System.err.println()
    }
}