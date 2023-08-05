package pikt.stdlib

import pikt.error.PiktIOException
import pikt.error.PiktWrongArgumentTypeException
import pikt.error.ValueType.FILE
import pikt.error.ValueType.STRING
import pikt.error.ValueType.or
import java.io.File

/**
 * Path of the source image.
 * This is automatically set.
 */
lateinit var sourceImagePath: String

/**
 * Path of the source image.
 */
fun getSourceImage() = sourceImagePath

/**
 * Instantiates a new [File].
 * @param pathOrFile either a [File] or a [String] path
 * @param requireExistance whether an exception should be thrown if the file does not exist
 */
fun newFile(pathOrFile: Any, requireExistance: Boolean = false): File {
    val file = when (pathOrFile) {
        is File -> pathOrFile
        is String -> File(pathOrFile)
        else -> throw PiktWrongArgumentTypeException(
            parameterName = "pathOrFile",
            argumentValue = pathOrFile,
            expectedType = STRING or FILE,
            reference = object {}
        )
    }

    if (requireExistance && !file.exists()) {
        throw PiktIOException("$file does not exist.", reference = object {})
    }

    return file
}