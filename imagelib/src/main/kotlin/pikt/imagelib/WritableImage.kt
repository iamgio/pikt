package pikt.imagelib

import pikt.error.PiktIOException
import java.io.File

/**
 * An image that can be modified and saved on file.
 */
interface WritableImage : Image {

    /**
     * Saves this image to file.
     * @param file file to save the image to
     * @throws PiktIOException if the image could not be saved
     */
    fun save(file: File)
}