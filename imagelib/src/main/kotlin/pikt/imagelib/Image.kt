package pikt.imagelib

import pikt.error.ImageValueType.IMAGE
import pikt.error.PiktWrongArgumentTypeException
import pikt.error.ValueType
import pikt.stdlib.newFile
import java.awt.image.BufferedImage
import java.io.File

/**
 * Instantiates a new writable [PiktImage].
 * @param width image width as [Int]
 * @param height image height as [Int]
 * @return readable and writable image
 */
fun newImage(width: Any, height: Any): PiktImage {
    if (width !is Int) {
        throw PiktWrongArgumentTypeException(
            parameterName = "width",
            argumentValue = width,
            expectedType = ValueType.NUMBER,
            reference = object {}
        )
    }
    if (height !is Int) {
        throw PiktWrongArgumentTypeException(
            parameterName = "height",
            argumentValue = height,
            expectedType = ValueType.NUMBER,
            reference = object {}
        )
    }

    return PiktImage(BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB))
}

/**
 * Instantiates a writable [PiktImage] from an image on disk.
 * @param pathOrFile either a [File] or a [String] path
 */
fun newImage(pathOrFile: Any) = PiktImage(newFile(pathOrFile, requireExistance = true))

/**
 * @return width of [image]
 */
fun imageWidth(image: Any): Int {
    if (image !is PiktImage) {
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
    if (image !is PiktImage) {
        throw PiktWrongArgumentTypeException(
            parameterName = "image",
            argumentValue = image,
            expectedType = IMAGE,
            reference = object {}
        )
    }

    return image.height
}