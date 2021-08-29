package pikt.stdlib

import java.io.File
import java.io.FileNotFoundException

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
    val file = when(pathOrFile) {
        is File -> pathOrFile
        is String -> File(pathOrFile)
        else -> throw RuntimeException("newFile: expected either a string or a file, ${pathOrFile.javaClass} found.")
    }
    if(requireExistance && !file.exists()) throw FileNotFoundException("$file does not exist.")

    return file
}