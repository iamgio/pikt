package eu.iamgio.pikt.explain.syntax

/**
 * A result of syntax highlighting pattern matching.
 *
 * @param content matched string
 * @param entry matching entry
 * @author Giorgio Garofalo
 */
data class SyntaxHighlightingMatch(
    val content: String,
    val entry: SyntaxHighlightingEntry
)