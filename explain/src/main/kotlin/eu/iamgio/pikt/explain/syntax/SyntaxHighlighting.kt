package eu.iamgio.pikt.explain.syntax

/**
 * A group of syntax highlighting rules.
 *
 * @param entries syntax highlighting rules
 * @author Giorgio Garofalo
 */
class SyntaxHighlighting(private val entries: Set<SyntaxHighlightingEntry>) {

    /**
     * @param text text to get matching groups for
     * @return groups as a `range-entry` map,
     *  where `range` refers to a range of character indexes within [text].
     */
    fun getGroups(text: String): Map<IntRange, SyntaxHighlightingMatch> {
        return this.entries.asSequence()
            .flatMap { entry ->
                entry.getMatchResults(text).map { match ->
                    match.range to SyntaxHighlightingMatch(match.value, entry)
                }
            }
            .toMap()
    }
}