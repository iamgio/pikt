package eu.iamgio.pikt.eval

import eu.iamgio.pikt.properties.ColorsProperty
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import java.io.File

/**
 * Utility functions for the standard library (stdlib module).
 *
 * @author Giorgio Garofalo
 */
object StdLib {

    /**
     * Stored name=colors map, initialized after [generateColorProperties] is called.
     */
    private lateinit var colors: Map<String, ColorsProperty>

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
     * Includes a library into the output JAR file.
     * @param libraryJarFile JAR file of the library to add
     * @param targetJarFile target output JAR file
     */
    fun copyJarLibrary(libraryJarFile: File, targetJarFile: File) {
        val targetJar = ZipFile(targetJarFile)
        val libraryJar = ZipFile(libraryJarFile) // TODO external libraries

        libraryJar.fileHeaders.filter { !it.fileName.startsWith("META-INF") }.forEach { header ->
            val inputStream = libraryJar.getInputStream(header)
            targetJar.addStream(inputStream, ZipParameters().apply { fileNameInZip = header.fileName; isIncludeRootFolder = true })
        }
    }
}