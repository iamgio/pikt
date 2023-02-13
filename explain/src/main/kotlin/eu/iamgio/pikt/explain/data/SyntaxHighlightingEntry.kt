package eu.iamgio.pikt.explain.data

import java.awt.Color

/**
 * A syntax highlighting rule.
 *
 * @param regex the pattern that specifies the syntax
 * @param color the color the given pattern should have
 * @author Giorgio Garofalo
 */
data class SyntaxHighlightingEntry(
    val regex: Regex,
    val color: Color
)