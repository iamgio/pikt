package eu.iamgio.pikt.lib

import java.io.File

/**
 * An external library, usually on the filesystem.
 *
 * @author Giorgio Garofalo
 */
interface Library {

    /**
     * Name of the library.
     */
    val name: String

    /**
     * Information about the library.
     */
    val info: LibraryInfo

    /**
     * The (optional) color scheme bundled into the library.
     */
    val colorScheme: LibraryColorScheme?

    /**
     * @return the functions contained in the library
     */
    fun getFunctions(): List<LibraryFunction>

    /**
     * Makes an existing executable on the filesystem able
     *     to access functions from the library.
     * @param executableFile executable file obtained from compilation
     */
    fun applyTo(executableFile: File)
}