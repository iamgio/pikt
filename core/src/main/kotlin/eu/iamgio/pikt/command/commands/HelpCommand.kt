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
     * Logs the content of a [member].
     */
    private fun logMember(member: HelpMember) {
        Log.info(formatTitle(member.name) + "\t\t" + member.description)
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