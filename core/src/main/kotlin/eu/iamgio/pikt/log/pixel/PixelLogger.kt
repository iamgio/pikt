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
     * Logs [pixel].
     */
    fun log(pixel: Pixel)

    /**
     * Goes on a new line.
     */
    fun newLine()

    /**
     * Logs the whole content of a [reader] from its start to its end via a copy.
     */
    fun logReader(reader: PixelReader) {
        val copy = reader.softCopy()
        copy.whileNotNull { log(it) }
        newLine()
    }
}