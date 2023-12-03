package pikt.imagelib

import pikt.error.PiktIndexOutOfBoundsException

/**
 * Abstraction of images and their manipulations.
 */
interface Image {

    /**
     * Width of the image, in pixels.
     */
    val width: Int

    /**
     * Height of the image, in pixels.
     */
    val height: Int

    /**
     * @param x X coordinate
     * @param y Y coordinate
     * @return the color of the image at the given coordinate
     * @throws PiktIndexOutOfBoundsException if one of the coordinates is negative or greater than the image size
     */
    fun getColor(x: Int, y: Int): Color
}