package pikt.error

import pikt.imagelib.PiktImage

/**
 * An exception thrown when an operation involving a [PiktImage] produced an error.
 *
 * @param message error message
 * @param image image that produced the error
 * @param reference a reference object used to retrieve the caller function, usually defined as `object {}`
 * @author Giorgio Garofalo
 */
class PiktImageException(message: String, image: PiktImage, reference: Any) : PiktException(
    """
        Image error: $message
        
        Image information:
            Size: ${image.width}x${image.height}
    """.trimIndent(),
    reference
)