package eu.iamgio.pikt.image

/**
 * Thrown when an error occurred while reading through a [PixelReader]
 *
 * @param message error message
 * @param reader the error source
 * @author Giorgio Garofalo
 */
class PixelReaderException(message: String, reader: PixelReader) : Exception(
    "$message (index ${reader.index} in statement ${reader.statement})"
)