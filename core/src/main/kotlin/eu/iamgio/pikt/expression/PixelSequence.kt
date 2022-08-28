package eu.iamgio.pikt.expression

import eu.iamgio.pikt.eval.Scope
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.image.PixelArray
import eu.iamgio.pikt.image.PixelReader

/**
 * A group of [Pixel]s, meant to be read as a sequence of data divided by a dot operator.
 *
 * @param pixels pixels contained in this sequence, in nesting order (a pixel with index `i` is nested within the pixel with index `i - 1`)
 * @see PixelReader.nextSequence
 */
class PixelSequence(pixels: Array<Pixel>) : PixelArray(pixels) {

    /**
     * The first pixel of the sequence
     */
    val first: Pixel?
        get() = pixels.firstOrNull()

    /**
     * The last pixel of the sequence
     */
    val last: Pixel?
        get() = pixels.lastOrNull()

    /**
     * Whether this sequence contains nested data.
     */
    val isNested: Boolean
        get() = size != 1

    /**
     * Converts this sequence of pixels to a Kotlin expression.
     * After the first pixel, each one is treated as a nested member of the previous one.
     * A nested member may be shown either as `first.second` (for structs) or as `first\[second\]` (for iterables),
     * depending on whether `second` is in the [scope] or not.
     *
     * @param scope scope that wraps this sequence
     * @return this sequence as a stringified Kotlin expression
     */
    fun toNestedCode(scope: Scope) = buildString {
        pixels.forEachIndexed { index, pixel ->
            when {
                index == 0          -> append(pixel)
                // If the pixel is in the current scope (or it's a number/character) it is used as index (for lists, strings and more): first[second].
                pixel in scope      -> append("[").append(pixel).append("]")
                pixel.isCharacter   -> append("[").append(pixel.characterContent).append("]")
                // If the pixel is not in the current scope, then it's read as a nested property, possibly of a struct: first.second.
                else                -> append(".").append(pixel)
            }
        }
    }
}