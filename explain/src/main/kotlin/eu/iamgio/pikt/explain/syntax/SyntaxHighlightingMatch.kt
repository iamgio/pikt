package eu.iamgio.pikt.explain.syntax

/**
 * A result of syntax highlighting pattern matching.
 *
 * @param content matched string
 * @param range range of matching character indexes
 * @param entry matching entry, or `null` if this match does not refer to a syntax highlighting group
 * @author Giorgio Garofalo
 */
data class SyntaxHighlightingMatch(
    val content: String,
    val range: IntRange,
    val entry: SyntaxHighlightingEntry?
)