package eu.iamgio.pikt.explain.data

/**
 * A comment on a specific point of the Pikt source image.
 *
 * @param x X coordinate of the target pixel
 * @param x Y coordinate of the target pixel
 * @param text text content of the comment
 * @author Giorgio Garofalo
 */
data class CommentData(
    val x: Int,
    val y: Int,
    val text: String
)
