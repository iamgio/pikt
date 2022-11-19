package eu.iamgio.pikt.image

/**
 * An immutable collection of [Pixel]s.
 *
 * @param pixels pixels contained in this collection
 * @author Giorgio Garofalo
 */
open class PixelArray(protected val pixels: List<Pixel>) : List<Pixel> by pixels {

    /**
     * Creates a copy of this array sliced from [start] to [end].
     * @return sliced copy of this array
     */
    fun sliced(start: Int, end: Int): PixelArray = PixelArray(pixels.subList(start, end))

    override fun toString() = "PixelArray(size=${pixels.size}, pixels=$pixels)"
}