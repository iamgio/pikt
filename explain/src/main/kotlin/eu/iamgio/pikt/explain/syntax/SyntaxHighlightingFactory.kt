package eu.iamgio.pikt.explain.syntax

/**
 * Factory for syntax highlighting rules.
 *
 * @param styleFactory factory to get the styles from
 * @author Giorgio Garofalo
 */
class SyntaxHighlightingFactory(private val styleFactory: SyntaxHighlightingStyleFactory) {

    /**
     * @return the default syntax highlighting set of rules
     */
    fun default() = SyntaxHighlighting(
        SyntaxHighlightingEntry(
            "\\{|}|->|,".toRegex(), this.styleFactory.lowRelevance()
        ),
        SyntaxHighlightingEntry(
            "var".toRegex(), this.styleFactory.keyword1()
        ),
        SyntaxHighlightingEntry(
            "\\b(if|else|return)\\b".toRegex(), this.styleFactory.keyword2()
        ),
        SyntaxHighlightingEntry(
            "forEach|\\b[a-zA-Z]+?(?=\\()".toRegex(), this.styleFactory.function()
        ),
        SyntaxHighlightingEntry(
            "\".+?\"".toRegex(), this.styleFactory.string()
        ),
        SyntaxHighlightingEntry(
            "//.+".toRegex(), this.styleFactory.comment()
        ),
    )
}