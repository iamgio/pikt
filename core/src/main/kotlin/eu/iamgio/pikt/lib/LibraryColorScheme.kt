package eu.iamgio.pikt.lib

import java.io.File
import java.io.InputStream
import java.util.*

/**
 * The color scheme (colors.properties) bundled in a [JarLibrary].
 *
 * @param library library containing this scheme
 * @param inputStream color scheme input stream
 * @author Giorgio Garofalo
 */
class LibraryColorScheme(private val library: JarLibrary, private val inputStream: InputStream) {

    /**
     * Color scheme as [Properties].
     */
    val properties: Properties
        get() = Properties().also { it.load(inputStream) }

    /**
     * Given a color scheme on disk, appends the content of this color scheme to it.
     * @param mainScheme color scheme to append keys to
     */
    fun appendToScheme(mainScheme: File) {
        val content = StringBuilder("\n# ${library.name}\n")

        // Read line-by-line the content of the scheme.
        inputStream.readBytes().decodeToString().lines().forEach {
            // If this line represents a property, add the default prefix + library prefix.
            val line = if(it.contains("=")) "$LIBRARY_COLOR_SCHEME_KEY_PREFIX${library.info.prefix}.$it" else it
            content.append("\n$line")
        }
        inputStream.close()

        // Append the content to the main color scheme.
        mainScheme.appendText(content.toString())
    }
}