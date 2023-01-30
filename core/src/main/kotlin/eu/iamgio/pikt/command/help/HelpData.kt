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
 * This usually represents a command, or a property or even a command argument.
 *
 * @param name name of the member
 * @param description description of the member
 * @param isOptional whether the member optional
 * @param defaultsTo the optional default value
 * @param values accepted values
 * @param args possible sub-members
 */
data class HelpMember(
    val name: String,
    val description: String,
    val isOptional: Boolean = false,
    val defaultsTo: String? = null,
    val values: List<String>? = null,
    val args: List<HelpMember> = emptyList()
)