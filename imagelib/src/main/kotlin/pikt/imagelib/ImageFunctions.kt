package pikt.imagelib

import pikt.error.ImageValueType.COLOR
import pikt.error.ImageValueType.IMAGE
import pikt.error.ImageValueType.WRITABLE_IMAGE
import pikt.error.PiktIOException
import pikt.error.PiktIndexOutOfBoundsException
import pikt.error.PiktWrongArgumentTypeException
import pikt.error.ValueType.NUMBER
import pikt.stdlib.newFile
import java.io.File

// Top-level functions to access image methods from Pikt.
// The only supported implementation is currently the AWT one for the JVM.

/**
 * @throws PiktWrongArgumentTypeException if [image] is not an [Image], or is not a [WritableImage] and [requireWritable] is `true`.
 */
private fun checkImageType(image: Any, requireWritable: Boolean = false, reference: Any) {
    if (image !is Image || (requireWritable && image !is WritableImage)) {
        throw PiktWrongArgumentTypeException(
            parameterName = "image",
            argumentValue = image,
            expectedType = if (requireWritable) WRITABLE_IMAGE else IMAGE,
            reference
        )
    }
}

/**
 * @throws PiktWrongArgumentTypeException if [x] and/or [y] are not numbers.
 */
private fun checkCoordinatesType(x: Any, y: Any, reference: Any) {
    if (x !is Int) {
        throw PiktWrongArgumentTypeException(
            parameterName = "x",
            argumentValue = x,
            expectedType = NUMBER,
            reference
        )
    }
    if (y !is Int) {
        throw PiktWrongArgumentTypeException(
            parameterName = "y",
            argumentValue = y,
            expectedType = NUMBER,
            reference
        )
    }
}

/**
 * Instantiates a new writable [Image].
 * @param width image width as [Int]
 * @param height image height as [Int]
 * @return a new readable and writable blank image
 */
fun newImage(width: Any, height: Any): WritableImage {
    if (width !is Int) {
        throw PiktWrongArgumentTypeException(
            parameterName = "width",
            argumentValue = width,
            expectedType = NUMBER,
            reference = object {}
        )
    }
    if (height !is Int) {
        throw PiktWrongArgumentTypeException(
            parameterName = "height",
            argumentValue = height,
            expectedType = NUMBER,
            reference = object {}
        )
    }

    return AwtImage.blank(width, height)
}

/**
 * Instantiates a writable [Image] from an image on disk.
 * @param pathOrFile either a [File] or a [String] path to load the image from
 * @return the loaded image
 * @throws PiktIOException if the image could not be read
 */
fun newImage(pathOrFile: Any): WritableImage = AwtImage.fromFile(newFile(pathOrFile, requireExistance = true))

/**
 * Saves an image to file.
 * @param pathOrFile either a [File] or a [String] path to save the image to
 * @throws PiktIOException if the image could not be saved
 */
fun saveImage(image: Any, pathOrFile: Any) {
    checkImageType(image, requireWritable = true, reference = object {})
    (image as WritableImage).save(newFile(pathOrFile))
}

/**
 * @return width of [image]
 */
fun imageWidth(image: Any): Int {
    checkImageType(image, reference = object {})
    return (image as Image).width
}

/**
 * @return height of [image]
 */
fun imageHeight(image: Any): Int {
    checkImageType(image, reference = object {})
    return (image as Image).height
}

/**
 * @param x X coordinate
 * @param y Y coordinate
 * @return the color of the [image] at the given coordinate
 * @throws PiktIndexOutOfBoundsException if one of the coordinates is negative or greater than the image size
 */
fun getImageColor(image: Any, x: Any, y: Any): Color {
    checkImageType(image, reference = object {})
    checkCoordinatesType(x, y, object {})

    return (image as Image).getColor(x as Int, y as Int)
}

/**
 * Changes the color of a pixel of the [image] at the given coordinates.
 * @param x X coordinate
 * @param y Y coordinate
 * @param color color to set
 * @throws PiktIndexOutOfBoundsException if one of the coordinates is negative or greater than the image size
 */
fun setImageColor(image: Any, x: Any, y: Any, color: Any) {
    checkImageType(image, requireWritable = true, reference = object {})
    checkCoordinatesType(x, y, reference = object {})

    if (color !is Color) {
        throw PiktWrongArgumentTypeException(
            parameterName = "color",
            argumentValue = color,
            expectedType = COLOR,
            reference = object {}
        )
    }

    return (image as WritableImage).setColor(x as Int, y as Int, color)
}