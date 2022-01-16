package eu.iamgio.pikt.image

/**
 * Represents a collection of [Pixel]s.
 *
 * @author Giorgio Garofalo
 */
open class PixelArray(protected val pixels: Array<Pixel>) {

    /**
     * Size of the array
     */
    val size: Int
        get() = pixels.size

    /**
     * Creates a copy of this array sliced from [start] to [end].
     * @return sliced copy of this array
     */
    fun sliced(start: Int, end: Int): PixelArray = PixelArray(pixels.sliceArray(IntRange(start, end)))

    /**
     * Gets a pixel from the array from given index.
     * @param index item index
     * @return pixel by index
     */
    operator fun get(index: Int): Pixel = pixels[index]

    override fun toString() = "PixelArray(size=${pixels.size}, pixels=[${pixels.joinToString()}])"
}

/**
 * A group of [Pixel]s, meant to be read as a sequence of data divided by a dot operator.
 *
 * @see PixelReader.nextSequence
 */
class PixelSequence(pixels: Array<Pixel>) : PixelArray(pixels) {

    val last: Pixel?
        get() = pixels.lastOrNull()

    override fun toString() = pixels.joinToString(separator = ".")
}