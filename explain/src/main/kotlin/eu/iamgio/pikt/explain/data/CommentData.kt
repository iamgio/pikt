package eu.iamgio.pikt.explain.data

/**
 * A comment on a specific point of the Pikt source image.
 *
 * @param T content type
 * @author Giorgio Garofalo
 */
interface CommentData<T> {

    /**
     * X coordinate of the target pixel.
     */
    val x: Int

    /**
     * Y coordinate of the target pixel
     */
    val y: Int

    /**
     * Content of the comment.
     */
    val content: T
}

/**
 * A text comment.
 *
 * @param x X coordinate of the target pixel
 * @param x Y coordinate of the target pixel
 * @param content text content
 * @author Giorgio Garofalo
 */
data class TextCommentData(
    override val x: Int,
    override val y: Int,
    override val content: String
) : CommentData<String>
