package eu.iamgio.pikt.command.commands

import com.diogonunes.jcolor.Ansi
import com.diogonunes.jcolor.Attribute
import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.command.help.HelpMember
import eu.iamgio.pikt.command.help.HelpSection
import eu.iamgio.pikt.command.help.YamlHelpDataParser
import eu.iamgio.pikt.log.Log

/**
 * The parsable resource that contains help data.
 */
private const val HELP_PATH = "/help.yml"

/**
 * Triggered by the -help argument.
 * Shows command and property information.
 *
 * @author Giorgio Garofalo
 */
class HelpCommand : Command("-help", closeOnComplete = true) {

    /**
     * Formats some [text] as a title.
     */
    private fun formatTitle(text: String): String {
        return Ansi.colorize(text, Attribute.BOLD())
    }

    /**
     * Formats some [text] as a subtitle.
     */
    private fun formatSubtitle(text: String): String {
        return Ansi.colorize(text, Attribute.WHITE_TEXT())
    }

    /**
     * Aligns [left] and [right] in two columns separated by a fixed [padding].
     * @param left text in the left column
     * @param right text in the right column
     * @param padding space between columns
     * @return text built by the two columns
     */
    private fun columns(left: String, right: String, padding: Int = 30, shift: Int = 0): String {
        val format = "%-${padding}s %s" // Align columns.
        return format.format("  ".repeat(shift) + formatTitle(left), right)
    }

    /**
     * Logs the content of a [member].
     */
    private fun logMember(member: HelpMember, level: Int = 0) {
        val description = buildString {
            if(member.isOptional) {
                append(formatSubtitle("(optional) "))
            }
            append(member.description)
        }

        // Supports recursively-accessed nested members.
        fun columns(left: String, right: String) = this.columns(left, right, shift = level)

        Log.info(columns(member.name, description))

        if(member.values != null) {
            Log.info(columns("Values:", member.values.joinToString(", "), shift = level + 1))
        }
        if(member.defaultsTo != null) {
            Log.info(columns("Default:", member.defaultsTo, shift = level + 1))
        }

        member.args.forEach {
            logMember(it, level + 1)
        }
    }

    /**
     * Logs the content of a [section], including its name, description and members.
     */
    private fun logSection(section: HelpSection) {
        Log.info(formatTitle(section.name))
        Log.info(formatSubtitle(section.description ?: ""))

        section.members.forEach { logMember(it) }
        Log.info("")
    }

    override fun execute(args: String?) {
        val inputStream = javaClass.getResourceAsStream(HELP_PATH)

        if(inputStream == null) {
            Log.error("Could not retrieve data.")
            return
        }

        val data = YamlHelpDataParser().parse(inputStream)
        data.sections.forEach { section ->
            logSection(section)
        }
    }
}