package eu.iamgio.pikt.image

/**
 * Pixel-by-pixel reader
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
}