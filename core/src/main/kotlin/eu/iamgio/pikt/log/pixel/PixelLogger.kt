package eu.iamgio.pikt.log.pixel

import eu.iamgio.pikt.image.Color
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.image.PixelReader

/**
 * A logger of colors and pixels.
 *
 * @author Giorgio Garofalo
 */
interface PixelLogger {

    /**
     * Logs a sequence of colors.
     * @param markIndex if not `null`, applies an implementation-determined mark at the given index.
     */
    fun logAll(colors: Iterable<Color>, markIndex: Int? = null)

    /**
     * Logs the colors of a sequence of pixels.
     * @param markIndex if not `null`, applies an implementation-determined mark at the given index.
     */
    fun logAll(pixels: List<Pixel>, markIndex: Int? = null) {
        logAll(pixels.map { it.color }, markIndex)
    }

    /**
     * Logs a single color.
     * @param mark if `true`, applies an implementation-determined mark.
     */
    fun log(color: Color, mark: Boolean = false) {
        logAll(setOf(color), 0.takeIf { mark })
    }

    /**
     * Logs the whole content of a [reader] from start to end.
     * @param markIndex if not `null`, applies an implementation-determined mark at the given index.
     */
    fun logReader(reader: PixelReader, markIndex: Int? = null) {
        logAll(reader.pixels, markIndex)
    }
}