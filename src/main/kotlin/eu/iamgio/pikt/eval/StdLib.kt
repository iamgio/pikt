package eu.iamgio.pikt.eval

import eu.iamgio.pikt.compiler.CompilationTarget
import eu.iamgio.pikt.properties.ColorsProperty
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Utility functions for the standard library (resources/pikt.stdlib/)
 *
 * @author Giorgio Garofalo
 */
object StdLib {

    /**
     * Stored name=colors map, initialized after [generateColorProperties] is called.
     */
    private lateinit var colors: Map<String, ColorsProperty>

    /**
     * List of libraries to be evaluated in [Evaluator.appendStdCode].
     */
    val libraryFiles = arrayOf(
            "Print",
            "Objects",
            "Numbers",
            "Lists"
    )

    /**
     * Generates a name=hex map for standard library members and stores it into [colors].
     * @param keys color properties keys
     * @param get function getting color value from key
     * @return standard library color scheme
     */
    fun generateColorProperties(keys: Set<Any>, get: (String) -> ColorsProperty): Map<String, ColorsProperty> {
        this.colors = keys
                .filter { it.toString().startsWith("stdlib.") }
                .associate { it.toString().split("stdlib.").last() to get(it.toString()) }
        return colors
    }

    /**
     * @param hex hexadecimal color to check
     * @return name of the stdlib member linked to [hex] color if exists. `null` otherwise
     */
    fun getMemberName(hex: String): String? {
        return colors.entries.firstOrNull { it.value.has(hex) }?.key
    }

    /**
     * Gets the target-specific library file from pikt.stdlib/targets.
     * @param target compilation target
     * @return target-specific [LibFile]
     */
    fun getTargetSpecificFile(target: CompilationTarget): LibFile {
        return LibFile("targets/" + when {
            target == CompilationTarget.JVM -> "jvm/JVM"
            target.isNative -> "native/Native"
            else -> ""
        })
    }

    /**
     * A standard library file (resources/pikt.stdlib/)
     *
     * @param name Kotlin file name without extension
     */
    data class LibFile(private val name: String) {

        /**
         * Reads the content of the library file skipping package declarations, imports and file warnings suppression.
         *
         * @return content as a string
         * @see generateColorProperties
         */
        fun readContent(): String {
            val builder = StringBuilder()

            val reader = BufferedReader(InputStreamReader(javaClass.getResourceAsStream("/pikt.stdlib/$name.kt")!!))
            while(reader.ready()) {
                val line = reader.readLine().let { line ->
                    if(line.startsWith("package") || line.startsWith("import") || line.startsWith("@file:")) {
                        // Skip package declaration, imports and file warnings suppression.
                        ""
                    } else {
                        line
                    }
                }
                builder.append(line).append("\n")
            }
            return builder.toString()
        }
    }
}