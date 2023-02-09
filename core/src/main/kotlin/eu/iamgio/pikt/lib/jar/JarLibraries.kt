package eu.iamgio.pikt.lib.jar

import eu.iamgio.pikt.lib.Libraries
import eu.iamgio.pikt.lib.Library
import java.io.File

/**
 * A collection of JAR libraries.
 *
 * @param libraries JAR libraries
 * @author Giorgio Garofalo
 */
class JarLibraries(private val libraries: List<JarLibrary>) : Libraries, Iterable<Library> by libraries {

    override fun getPath(separator: String): String {
        return libraries.joinToString(File.pathSeparator) { it.absolutePath }
    }
}