package eu.iamgio.pikt.command.help

/**
 * Contains information about commands or other data.
 *
 * @param sections provided sections
 * @see eu.iamgio.pikt.command.commands.HelpCommand
 */
data class HelpData(
    val sections: List<HelpSection>
)

/**
 * Represents a group of similar members (e.g. commands).
 *
 * @param name name of the section
 * @param description optional description of the section
 * @param members members of this section
 */
data class HelpSection(
    val name: String,
    val description: String?,
    val members: List<HelpMember>
)

/**
 * An element to be explained within some help data.
 * This usually represents a command or a property.
 *
 * @param name name of the member
 * @param description description of the member
 */
data class HelpMember(
    val name: String,
    val description: String,
    val isOptional: Boolean = false
)