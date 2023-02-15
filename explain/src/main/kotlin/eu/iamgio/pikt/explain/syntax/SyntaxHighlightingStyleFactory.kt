package eu.iamgio.pikt.explain.syntax

import java.awt.Color

/**
 * Factory for syntax highlighting styles.
 *
 * @author Giorgio Garofalo
 */
interface SyntaxHighlightingStyleFactory {

    /**
     * @return style for unimportant characters and symbols
     */
    fun lowRelevance(): SyntaxHighlightingEntryStyle

    /**
     * @return style for some kind of keywords
     */
    fun keyword1(): SyntaxHighlightingEntryStyle

    /**
     * @return style for some kind of keywords
     */
    fun keyword2(): SyntaxHighlightingEntryStyle

    /**
     * @return style for function names
     */
    fun function(): SyntaxHighlightingEntryStyle

    /**
     * @return style for string initializations
     */
    fun string(): SyntaxHighlightingEntryStyle

    /**
     * @return style for comments
     */
    fun comment(): SyntaxHighlightingEntryStyle
}

/**
 * The default implementation of a [SyntaxHighlightingStyleFactory].
 *
 * @author Giorgio Garofalo
 */
object DefaultSyntaxHighlightingStyleFactory : SyntaxHighlightingStyleFactory {

    override fun lowRelevance() = SyntaxHighlightingEntryStyle(
        color = Color(161, 161, 161)
    )

    override fun keyword1() = SyntaxHighlightingEntryStyle(
        color = Color(255, 154, 154)
    )

    override fun keyword2() = SyntaxHighlightingEntryStyle(
        color = Color(255, 223, 126)
    )

    override fun function() = SyntaxHighlightingEntryStyle(
        color = Color(190, 255, 154)
    )

    override fun string() = SyntaxHighlightingEntryStyle(
        color = Color(206, 255, 179)
    )

    override fun comment() = SyntaxHighlightingEntryStyle(
        color = Color(155, 110, 158)
    )
}