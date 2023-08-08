package pikt.error

import pikt.imagelib.Image

/**
 * An exception thrown when an operation involving an [Image] produced an error.
 *
 * @param message error message
 * @param image image that produced the error
 * @param reference a reference object used to retrieve the caller function, usually defined as `object {}`
 * @author Giorgio Garofalo
 */
class PiktImageException(message: String, image: Image, reference: Any) : PiktException(
    """
        Image error: $message
        
        Image information:
            Size: ${image.width}x${image.height}
    """.trimIndent(),
    reference
)