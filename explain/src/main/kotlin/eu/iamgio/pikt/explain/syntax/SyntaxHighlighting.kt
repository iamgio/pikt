package eu.iamgio.pikt.explain.syntax

/**
 * @author Giorgio Garofalo
 */
class SyntaxHighlighting(private val entries: Set<SyntaxHighlightingEntry>) {

    /**
     * @param text text to get matching groups for
     * @return groups as an `range-entry` map,
     *  where `range` refers to a range of character indexes within [text].
     */
    fun getGroups(text: String): Map<IntRange, SyntaxHighlightingEntry> {
        return this.entries.asSequence()
            .mapNotNull { entry ->
                entry.getMatchResult(text)?.range?.let { it to entry }
            }
            .toMap()
    }
}