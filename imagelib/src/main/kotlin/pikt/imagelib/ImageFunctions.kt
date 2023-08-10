package pikt.imagelib

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
    if (image !is WritableImage) {
        throw PiktWrongArgumentTypeException(
            parameterName = "image",
            argumentValue = image,
            expectedType = WRITABLE_IMAGE,
            reference = object {}
        )
    }

    image.save(newFile(pathOrFile))
}

/**
 * @return width of [image]
 */
fun imageWidth(image: Any): Int {
    if (image !is Image) {
        throw PiktWrongArgumentTypeException(
            parameterName = "image",
            argumentValue = image,
            expectedType = IMAGE,
            reference = object {}
        )
    }

    return image.width
}

/**
 * @return height of [image]
 */
fun imageHeight(image: Any): Int {
    if (image !is Image) {
        throw PiktWrongArgumentTypeException(
            parameterName = "image",
            argumentValue = image,
            expectedType = IMAGE,
            reference = object {}
        )
    }

    return image.height
}

/**
 * @param x X coordinate
 * @param y Y coordinate
 * @return the color of the [image] at the given coordinate
 * @throws PiktIndexOutOfBoundsException if one of the coordinates is negative or greater than the image size
 */
fun imageColor(image: Any, x: Any, y: Any): Color {
    if (image !is Image) {
        throw PiktWrongArgumentTypeException(
            parameterName = "image",
            argumentValue = image,
            expectedType = IMAGE,
            reference = object {}
        )
    }

    if (x !is Int) {
        throw PiktWrongArgumentTypeException(
            parameterName = "x",
            argumentValue = x,
            expectedType = NUMBER,
            reference = object {}
        )
    }
    if (y !is Int) {
        throw PiktWrongArgumentTypeException(
            parameterName = "y",
            argumentValue = y,
            expectedType = NUMBER,
            reference = object {}
        )
    }

    return image.getColor(x, y)
}