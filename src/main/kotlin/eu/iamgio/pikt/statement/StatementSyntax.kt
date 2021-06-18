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
     * @param mark mutable mark type
     */
    open class Member(val name: String, val type: Type, var mark: Mark = Mark.UNSET) {

        /**
         * Formatted member based on its type.
         */
        open val asSyntaxString = type.transform(name)
    }

    /**
     * A group of members.
     *
     * @param name invisible name, used as an ID
     * @param type group type
     * @param mark mark of the group
     * @param members list of members the group contains
     */
    class MemberGroup(name: String, type: Type, mark: Mark = Mark.UNSET, private vararg val members: Member) : Member(name, type, mark) {

        override val asSyntaxString: String
            get() = type.transform(members.joinToString(separator = " ") { it.asSyntaxString })
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