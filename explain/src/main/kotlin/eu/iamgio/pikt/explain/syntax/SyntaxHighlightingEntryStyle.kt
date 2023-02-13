package eu.iamgio.pikt.explain.syntax

import java.awt.Color

/**
 * The visual style of a syntax highlighting entry.
 *
 * @param color the color the given pattern should have
 * @author Giorgio Garofalo
 */
data class SyntaxHighlightingEntryStyle(
    val color: Color
)