package eu.iamgio.pikt.eval

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Utility functions for the standard library (resources/pikt.stdlib/)
 *
 * @author Giorgio Garofalo
 */
object StdLib {

    /**
     * List of libraries to be evaluated in [Evaluator.appendStdCode].
     */
    val libraryFiles = arrayOf(
            "print"
    )

    /**
     * Generates a name=hex map for standard library members.
     * @param keys color properties keys
     * @param get function getting color value from key
     * @return standard library color scheme
     */
    fun generateColorProperties(keys: Set<Any>, get: (String) -> String): Map<String, String> = keys
            .filter { it.toString().startsWith("stdlib.") }
            .map { it.toString().split("stdlib.").last() to get(it.toString()) }
            .toMap()

    /**
     * A standard library file (resources/pikt.stdlib/)
     *
     * @param name Kotlin file name without extension
     */
    class LibFile(private val name: String) {

        /**
         * Reads the content of the library file and changes placeholders with [colors] values.
         *
         * @param colors color scheme
         * @return content as a string
         * @see generateColorProperties
         */
        fun readContent(colors: Map<String, String>): String {
            val builder = StringBuilder()

            val reader = BufferedReader(InputStreamReader(javaClass.getResourceAsStream("/pikt.stdlib/$name.kt")))
            while(reader.ready()) {
                val line = reader.readLine().let { line ->
                    if(line.startsWith("package")) {
                        // Skip package declaration
                        ""
                    } else {
                        // Searches for a top-level member (either function or variable/constant)
                        Regex("(?<=(fun|val|var) )\\w+").find(line)?.groups?.firstOrNull()?.let { group ->

                            // Getting member name (e.g. fun print(...) -> print),
                            // fetching hex value from color schemes and replacing it.

                            colors[group.value]?.let { hex ->
                                line.replaceRange(group.range, "`$hex`")
                            }

                        } ?: line // If there is not a match (regular code) do nothing
                    }
                }
                builder.append(line).append("\n")
            }
            return builder.toString()
        }
    }
}