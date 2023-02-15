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
    fun getGroups(text: String): List<SyntaxHighlightingMatch> {
        // Groups with a syntax highlighting match.
        val matchGroups = this.entries.asSequence()
            .flatMap { entry ->
                entry.getMatchResults(text).map { match ->
                    SyntaxHighlightingMatch(match.value, match.range, entry)
                }
            }.toList()

        // Groups that don't have any syntax highlighting.
        val remainingGroups = this.getComplementaryRanges(0..text.length, matchGroups)
            .map { SyntaxHighlightingMatch(text.substring(it), it, entry = null) }

        return matchGroups + remainingGroups
    }

    /**
     * Example: `fullRange = 0..10; groups (ranges) = [2..5, 7..8]; result = [0..2, 6..7, 9..10]`
     * @param fullRange int range to subtract values from
     * @param groups groups to subtract from the full range
     * @return sub-ranges of [fullRange] that don't belong to any of the given groups
     */
    private fun getComplementaryRanges(fullRange: IntRange, groups: List<SyntaxHighlightingMatch>) = buildList {
        var lastIndex = 0

        // Updates the complementary ranges with the one
        // between the last range end and `index`
        fun addComplementaryBeforeIndex(index: Int) {
            if(index > lastIndex) {
                add(lastIndex until index)
            }
        }

        // Step-by-step complementary.
        groups.asSequence()
            .sortedBy { it.range.first }
            .forEach { group ->
                addComplementaryBeforeIndex(group.range.first)
                lastIndex = group.range.last + 1
            }
        // Remaining range.
        addComplementaryBeforeIndex(fullRange.last)
    }
}