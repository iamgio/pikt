package eu.iamgio.pikt.lib.jar

import eu.iamgio.pikt.lib.Libraries
import eu.iamgio.pikt.lib.Library
import java.io.File
import java.net.URLClassLoader

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

    /**
     * A class loader that includes all the libraries.
     */
    private val classLoader: ClassLoader by lazy {
        val urls = libraries.map { it.file.toURI().toURL() }
        URLClassLoader.newInstance(urls.toTypedArray(), javaClass.classLoader)
    }

    init {
        // Class loader injection.
        libraries.forEach { it.classLoader = this.classLoader }
    }
}