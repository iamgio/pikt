package eu.iamgio.pikt.project

import org.yaml.snakeyaml.Yaml
import java.io.InputStream

/**
 * YAML project information:
 * ```
 * properties: ...
 * commands: ...
 * tasks: ...
 * ```
 */
private typealias InfoData = Map<String, *>

/**
 * YAML task sub-structure information:
 * ```
 * - task_name:
 *   [nested project info data here]
 * ```
 */
private typealias TaskData = Map<String, InfoData>

/**
 * File parser for [PiktProjectInfo].
 *
 * @param inputStream raw YAML file data
 * @author Giorgio Garofalo
 */
class PiktProjectInfoParser(private val inputStream: InputStream) {

    private fun load(): InfoData = Yaml().load(inputStream)

    /**
     * Converts a list from the configuration into a map.
     * A list may contain:
     * - Simple data (i.e. strings): the data is converted into {x=null}
     * - Key-value pairs(s): the data is converted into {key=value}
     */
    private fun listToMap(list: List<*>?): InfoData {
        val map = hashMapOf<String, Any?>()
        list?.forEach {
            when(it) {
                is Map<*, *> -> map.putAll(it.mapKeys { entry -> entry.key.toString() })
                else -> map[it.toString()] = null
            }
        }
        return map
    }

    /**
     * @param tasksList raw list of name=data values
     * @return tasks as raw list to a name=data map
     */
    @Suppress("UNCHECKED_CAST")
    private fun parseTask(tasksList: List<*>): PiktProjectTasks {
        return tasksList.mapNotNull {
            (it as? TaskData)?.mapNotNull { (name, info) ->
                name to parse(info, includeTasks = false)
            }?.toMap()
        }.flatMap { it.asSequence() }.associate { it.key to it.value }
    }

    /**
     * @param data YAML section to extract info from. Defaults to the top-level (global) section.
     * @param includeTasks whether tasks should be parsed. Generally, tasks are exclusive to the top level and are not parsed recursively
     * @return [inputStream] raw file into a [PiktProjectInfo] containing properties and commands maps.
     */
    fun parse(data: Map<String, *> = load(), includeTasks: Boolean = true): PiktProjectInfo {
        val properties = data["properties"] as? List<*>
        val commands = data["commands"] as? List<*>

        var tasks: PiktProjectTasks? = null
        if(includeTasks) {
            val tasksList = data["tasks"] as? List<*>
            tasks = tasksList?.let { parseTask(it) }
        }

        return PiktProjectInfo(listToMap(properties), listToMap(commands), tasks)
    }
}