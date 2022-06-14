package eu.iamgio.pikt.properties

import org.yaml.snakeyaml.Yaml
import java.io.InputStream

/**
 * Project info allows to load properties and commands to use for a single Pikt project.
 *
 * An example file can be found at /test/resources/example-project.yml
 *
 * @param properties name=value properties
 * @param commands name=args commands
 * @author Giorgio Garofalo
 */
data class PiktProjectInfo(val properties: Map<String, *>, val commands: Map<String, *>) {

    /**
     * Saves the given [properties] into [System] properties.
     */
    fun applyProperties() {
        properties.forEach { (property, value) ->
            System.setProperty(property, value?.toString() ?: "")
        }
    }

    /**
     * Generates a command-line-like command.
     *
     * Example: {name=args} is converted to -name=args
     */
    private fun generateCommandsStrings(): List<String> {
        return commands.map { "-${it.key}" + if(it.value != null) "=${it.value}" else "" }
    }

    companion object {
        /**
         * If the project info exists, its commands are merged with another array of strings.
         * @param args array to merge
         * @return merged array if project info is not `null`, [args] otherwise.
         */
        fun PiktProjectInfo?.mergeArgs(args: Array<String>) =
                if(this == null) args else (generateCommandsStrings() + args).toTypedArray()
    }
}

/**
 * File parser for [PiktProjectInfo].
 *
 * @param inputStream raw YAML file data
 * @author Giorgio Garofalo
 */
class PiktProjectInfoParser(private val inputStream: InputStream) {

    private fun load(): Map<String, *> = Yaml().load(inputStream)

    /**
     * Converts a list from the configuration into a map.
     * A list may contain:
     * - Simple data (i.e. strings): the data is converted into {x=null}
     * - Key-value pairs(s): the data is converted into {key=value}
     */
    private fun listToMap(list: List<*>?): Map<String, *> {
        val map = hashMapOf<String, Any?>()
        list?.forEach {
            when(it) {
                is Map<*, *> -> map.putAll(it.mapKeys { entry -> entry.key.toString()})
                else -> map[it.toString()] = null
            }
        }
        return map
    }

    /**
     * @return [inputStream] raw file into a [PiktProjectInfo] containing properties and commands maps.
     */
    fun parse(): PiktProjectInfo {
        val data = load()
        val properties = data["properties"] as? List<*>
        val commands = data["commands"] as? List<*>
        return PiktProjectInfo(listToMap(properties), listToMap(commands))
    }
}