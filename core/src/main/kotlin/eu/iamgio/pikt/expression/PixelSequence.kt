package eu.iamgio.pikt.expression

import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.image.PixelArray
import eu.iamgio.pikt.image.PixelReader

/**
 * A group of [Pixel]s, meant to be read as a sequence of data divided by a dot operator.
 *
 * @param pixels pixels contained in this sequence, in nesting order (a pixel with index `i` is nested within the pixel with index `i - 1`)
 * @see PixelReader.nextSequence
 */
class PixelSequence(pixels: List<Pixel>) : PixelArray(pixels) {

    /**
     * Whether this sequence contains nested data.
     */
    val isNested: Boolean
        get() = size != 1
}