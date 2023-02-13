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
) {

    /**
     * @param text text to find occurrences in
     * @return pattern match result, if it exists
     */
    fun getMatchResult(text: String): MatchResult? {
        return this.regex.find(text)
    }
}