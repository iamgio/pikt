package eu.iamgio.pikt.project

import eu.iamgio.pikt.command.RawCommandsMap

/**
 * A task has a name (key) and properties/commands (value)
 */
typealias PiktProjectTasks = Map<String, PiktProjectInfo>

/**
 * Project info allows to load properties and commands to use for a single Pikt project.
 *
 * An example file can be found at /test/resources/example-project.yml
 *
 * @param properties name=value properties
 * @param commands name=args commands
 * @param tasks executable named jobs
 * @author Giorgio Garofalo
 */
data class PiktProjectInfo(val properties: Map<String, *>, val commands: RawCommandsMap, private val tasks: PiktProjectTasks?) {

    /**
     * Saves the given [properties] into [System] properties, without overriding command line settings.
     */
    fun applyProperties() {
        properties.forEach { (property, value) ->
            if(System.getProperty(property) == null) {
                System.setProperty(property, value?.toString() ?: "")
            }
        }
    }

    /**
     * @param taskName name of the task to retrieve
     * @return project task by name (if it exists)
     */
    fun getTaskByName(taskName: String): PiktProjectInfo? = tasks?.get(taskName)
}