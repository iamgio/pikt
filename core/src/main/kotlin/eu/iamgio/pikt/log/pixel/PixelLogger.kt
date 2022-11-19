package eu.iamgio.pikt.log.pixel

import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.image.PixelReader

/**
 * A logger of pixels.
 *
 * @author Giorgio Garofalo
 */
interface PixelLogger {

    /**
     * Whether there should be empty lines (via [newLine]) before and after logging pixel sequences.
     */
    val surroundByEmptyLines: Boolean
        get() = false

    /**
     * Logs [pixel].
     * @param mark whether a mark should be applied on the output
     */
    fun log(pixel: Pixel, mark: Boolean = false)

    /**
     * Goes on a new line.
     */
    fun newLine()

    /**
     * Logs the whole content of a [reader] from its start to its end via a copy.
     * @param markIndex if not `null`, applies a mark on the pixel of the given index
     */
    fun logReaderWithMark(reader: PixelReader, markIndex: Int? = null) {
        if(surroundByEmptyLines) newLine()

        val copy = reader.softCopy() // Copies the reader with its index set to 0
        var index = 0
        copy.whileNotNull {
            log(it, mark = index == markIndex)
            index++
        }
        newLine()

        if(surroundByEmptyLines) newLine()
    }

    /**
     * Logs the whole content of a [reader] from its start to its end via a copy.
     */
    fun logReader(reader: PixelReader) = logReaderWithMark(reader, null)

    companion object {
        /**
         * The active, global, pixel logger to be used.
         */
        var currentLogger: PixelLogger? = null
    }

    /**
     * Pixel logger types.
     *
     * @param newLogger corresponding logger factory.
     */
    @Suppress("unused")
    enum class Type(val newLogger: () -> PixelLogger) {
        RGB({ RGBConsolePixelLogger(System.err) }),
        BOX({ AsciiBoxConsolePixelLogger(System.err) })
    }
}