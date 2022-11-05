package eu.iamgio.pikt.image

/**
 * Represents a collection of [Pixel]s.
 *
 * @author Giorgio Garofalo
 */
open class PixelArray(protected val pixels: Array<Pixel>) {

    /**
     * Size of the array.
     */
    val size: Int
        get() = pixels.size

    /**
     * Whether the array is empty.
     */
    val isEmpty: Boolean
        get() = pixels.isEmpty()

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