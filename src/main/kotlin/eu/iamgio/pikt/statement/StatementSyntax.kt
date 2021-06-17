package eu.iamgio.pikt.statement

/**
 * Represents a statement syntax.
 *
 * @param members syntax parts
 * @author Giorgio Garofalo
 */
class StatementSyntax(private vararg val members: Member) {

    /**
     * Types of syntax members.
     * @param transform function that converts a member's name to a formatted syntax member
     */
    enum class Type(val transform: (String) -> String) {
        OBLIGATORY       ({ "<$it>"     }),
        OPTIONAL         ({ "<$it?>"    }),
        SCHEME_OBLIGATORY({ "<%$it%>"   }),
        SCHEME_OPTIONAL  ({ "<%$it%>"   }),
        VARARG_OBLIGATORY({ "<...$it>"  }),
        VARARG_OPTIONAL  ({ "<...$it?>" }),
    }

    /**
     * Changes the mark of a member, gotten via its [name].
     * @param name member name
     * @param mark mark to set
     */
    fun mark(name: String, mark: Mark) {
        members.first { it.name == name }.mark = mark
    }

    /**
     * A line that contains properly spaced marks, centered to their corresponding members from [toString].
     * Its behavior on non-monospaced fonts may be unexpected.
     */
    val marksLine: String
        get() {
            val builder = StringBuilder()
            members.forEach { member ->
                val length = member.asSyntaxString.length
                builder.append(" ".repeat(length.floorDiv(2)))
                builder.append(member.mark.char)
                builder.append(" ".repeat(length.floorDiv(2)))
            }
            return builder.toString()
        }

    /**
     * @return Syntax members as a string
     */
    override fun toString() = members.joinToString(separator = " ") { it.asSyntaxString }

    /**
     * A part of a statement syntax.
     *
     * @param name member name
     * @param type member type
     */
    data class Member(val name: String, val type: Type) {

        /**
         * Formatted member based on its type.
         */
        val asSyntaxString = type.transform(name)

        /**
         * Mutable mark of this member.
         */
        var mark = Mark.UNSET
    }

    /**
     * Different types of marks of a syntax member.
     *
     * @param char character of this mark
     */
    enum class Mark(val char: Char) {
        /**
         * If the member was properly used.
         */
        CORRECT('✓'),

        /**
         * If the member was not properly used.
         */
        WRONG('✗'),

        /**
         * If the member was not set.
         */
        UNSET(' ')
    }
}