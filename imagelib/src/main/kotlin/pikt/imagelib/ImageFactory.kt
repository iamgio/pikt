package pikt.imagelib

import pikt.error.PiktIOException
import java.io.File

/**
 * A factory for image creation and loading.
 *
 * @param T type of the images to create or load
 */
interface ImageFactory<T : Image> {

    /**
     * Instantiates a blank image of the given size.
     * @param width image width
     * @param height image height
     * @param transparent whether the image supports transparency
     */
    fun blank(width: Int, height: Int, transparent: Boolean = false): T

    /**
     * Loads an image from file.
     * @param file file to load the image from
     * @return the loaded image
     * @throws PiktIOException if the image could not be loaded
     */
    fun fromFile(file: File): T
}