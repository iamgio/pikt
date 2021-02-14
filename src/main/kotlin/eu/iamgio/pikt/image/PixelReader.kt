package eu.iamgio.pikt.image

import eu.iamgio.pikt.properties.ColorsProperties

/**
 * Pixel-by-pixel reader of a [PiktImage]
 *
 * @param pixels collection of [Pixel]s
 * @author Giorgio Garofalo
 */
class PixelReader(private val pixels: PixelArray) {

    /**
     * Current pixel index.
     */
    private var index: Int = -1

    /**
     * Gets the next non-whitespace pixel available.
     * @return next pixel, <tt>null</tt> if there is none
     */
    fun next(): Pixel? {
        index++
        if(index >= pixels.size) return null

        return pixels[index].let { pixel ->
            if(pixel.isWhitespace()) {
                next()
            } else {
                pixel
            }
        }
    }

    /**
     * Subdivides this reader into one minor reader for each statement.
     * @return list of minor readers.
     */
    fun subdivide(colors: ColorsProperties): List<PixelReader> {
        val readers = mutableListOf<PixelReader>()

        var startIndex = 0

        while(true) {
            val pixel = next()
            if(startIndex != index && (pixel == null || pixel.getStatement(colors) != null)) {
                readers += PixelReader(pixels.sliced(startIndex, index - 1))
                startIndex = index
            }
            if(pixel == null) return readers
        }
    }

    /**
     * Prints an error preceded by a standard prefix
     */
    fun error(message: String) = System.err.println("Error at index $index: $message")
}