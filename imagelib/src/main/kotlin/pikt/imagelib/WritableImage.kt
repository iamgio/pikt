package pikt.imagelib

import pikt.error.PiktIOException
import pikt.error.PiktIndexOutOfBoundsException
import java.io.File

/**
 * An image that can be modified and saved on file.
 */
interface WritableImage : Image {

    /**
     * Changes the color of a pixel.
     * @param x X coordinate
     * @param y Y coordinate
     * @param color color to set at the given coordinates
     * @throws PiktIndexOutOfBoundsException if one of the coordinates is negative or greater than the image size
     */
    fun setColor(x: Int, y: Int, color: Color)

    /**
     * Saves this image to file.
     * @param file file to save the image to
     * @throws PiktIOException if the image could not be saved
     */
    fun save(file: File)
}