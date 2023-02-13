package eu.iamgio.pikt.explain.syntax

/**
 * A syntax highlighting rule.
 *
 * @param regex the pattern that specifies the syntax
 * @param style the style the given pattern should have
 * @author Giorgio Garofalo
 */
data class SyntaxHighlightingEntry(
    val regex: Regex,
    val style: SyntaxHighlightingEntryStyle
)