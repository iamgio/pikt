package eu.iamgio.pikt.command.help

import eu.iamgio.pikt.log.Log
import org.yaml.snakeyaml.Yaml
import java.io.InputStream

/**
 * YAML implementation of the help data parser.
 *
 * @author Giorgio Garofalo
 */
class YamlHelpDataParser : HelpDataParser {

    /**
     * Parses members (e.g. commands) defined by a name, a description and possibly other data.
     */
    private fun parseMembers(membersData: List<*>): List<HelpMember> {
        return membersData.map { memberData ->
            val (memberName, member) = (memberData as Map<*, *>).entries.first()
            val name = memberName?.toString() ?: "<no name>"

            when(member) {
                is String -> HelpMember(name, description = member)
                is Map<*, *> -> {
                    // Here there could be support for arguments explanation.
                    HelpMember(
                        name,
                        description = member["description"]?.toString() ?: "<no description>",
                        isOptional = (member["optional"] as? Boolean) ?: false,
                        defaultsTo = member["default"]?.toString(),
                        values = (member["values"] as? List<*>)?.map { it.toString() },
                        args = (member["args"] as? List<*>)?.let { parseMembers(it) } ?: emptyList()
                    )
                }
                else -> {
                    Log.warn("Invalid member found while parsing help data")
                    return emptyList()
                }
            }
        }
    }

    /**
     * Parses a list of sections. A section is defined by a name, description and list of members.
     */
    private fun parseSections(sectionsData: List<*>): List<HelpSection> {
        return sectionsData.map { section ->
            if(section !is Map<*, *>) {
                Log.warn("Invalid section found while parsing help data.")
                return emptyList()
            }
            HelpSection(
                name = section["name"]?.toString() ?: "<no name>",
                description = section["description"]?.toString(),
                members = this.parseMembers(section["members"] as List<*>)
            )
        }
    }

    override fun parse(inputStream: InputStream): HelpData {
        val data: Map<String, *> = Yaml().load(inputStream)
        val sections = this.parseSections(data["sections"] as List<*>)

        return HelpData(sections)
    }
}