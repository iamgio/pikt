package eu.iamgio.pikt.image

import eu.iamgio.pikt.eval.Scope
import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.ExpressionParser
import eu.iamgio.pikt.expression.ExpressionType
import eu.iamgio.pikt.expression.PixelSequence
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.log.pixel.ConsolePixelLogger
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.StatementSyntax

/**
 * Pixel-by-pixel reader of a [PiktImage]
 *
 * @param pixels collection of [Pixel]s
 * @param statement statement of this reader, if already subdivided
 * @author Giorgio Garofalo
 */
class PixelReader(private val pixels: PixelArray, val statement: Statement? = null) {

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
    fun softCopy(): PixelReader = PixelReader(pixels, statement)

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
        return PixelSequence(sequence)
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
        while(nextSequence().also { sequence = it }.isNotEmpty()) {
            task(sequence)
        }
    }

    /**
     * Creates a copy of this reader sliced from [start] to [end].
     * @return sliced copy of this reader
     */
    fun sliced(start: Int, end: Int, statement: Statement? = null) = PixelReader(pixels.sliced(start, if(end < size) end else size - 1), statement)

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
     * @param pixelIndex index of the pixel that caused the error
     */
    fun error(message: String, syntax: StatementSyntax? = null, pixelIndex: Int) {
        isInvalidated = true

        // Where the error happened.
        val coordinates = if(pixels.isNotEmpty()) {
            val pixel = pixels.getOrNull(pixelIndex) ?: pixels.last()
            " at (${pixel.x},${pixel.y})"
        } else ""

        Log.error("Error$coordinates (index $pixelIndex in ${statement?.name ?: "<no statement>"}):")
        Log.error("\t$message")

        // Prints a nice message that explains the expected syntax vs the used syntax.
        // Example from SetVariableStatement:
        //
        // Syntax: <%variable.set%> <name> <value>
        //	               ✓           ✓      ✗
        if(syntax != null) {
            val prefix = "Syntax: "
            Log.error("\t" + prefix + syntax)
            Log.error("\t" + " ".repeat(prefix.length) + syntax.marksLine)
        }

        // Logs the pixels of this reader with the selected logger.
        Log.pixelLogger?.let { logger ->
            (logger as? ConsolePixelLogger)?.stream = System.err
            logger.logReaderWithMark(this, markIndex = pixelIndex)
        }

        Log.error("")
    }

    /**
     * Prints an error preceded by a standard prefix and invalidates this reader.
     * `Error at (x,y) (index i in Statement): message`
     * @param message message to log
     * @param syntax optional [statement]'s syntax that should be printed out
     * @param referenceToFirstPixel whether the error must reference the first pixel in the reader
     */
    fun error(message: String, syntax: StatementSyntax? = null, referenceToFirstPixel: Boolean = false) {
        error(message, syntax, if(referenceToFirstPixel) 0 else this.index - 1)
    }

    /**
     * Prints an error preceded by a standard prefix and invalidates this reader.
     * @param message message to log
     * @param syntax optional [statement]'s syntax that should be printed out
     * @param atPixel pixel that caused the error (contained within this reader)
     */
    fun error(message: String, syntax: StatementSyntax? = null, atPixel: Pixel) {
        val copy = this.softCopy() // A soft copy starts from index 0.

        // Search for target pixel
        copy.whileNotNull {
            if(it === atPixel) error(message, syntax, copy.index)
        }

        // Fallback if the target pixel was not found.
        if(!isInvalidated) {
            error(message, syntax, referenceToFirstPixel = true)
        }
    }
}