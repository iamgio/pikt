package pikt.imagelib

import pikt.error.ImageValueType.IMAGE
import pikt.error.PiktWrongArgumentTypeException
import pikt.error.ValueType.NUMBER
import pikt.stdlib.newFile
import java.io.File

// Top-level functions to access image methods from Pikt.

/**
 * Instantiates a new writable [Image].
 * @param width image width as [Int]
 * @param height image height as [Int]
 * @return a new readable and writable image
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

    return PiktImage.blank(width, height)
}

/**
 * Instantiates a writable [Image] from an image on disk.
 * @param pathOrFile either a [File] or a [String] path
 * @return the loaded image
 */
fun newImage(pathOrFile: Any) = PiktImage.fromFile(newFile(pathOrFile, requireExistance = true))

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