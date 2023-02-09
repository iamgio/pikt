package eu.iamgio.pikt.lib

import java.io.File

/**
 * A collection of external libraries.
 *
 * @author Giorgio Garofalo
 */
interface Libraries : Iterable<Library> {

    /**
     * @param separator separator between single paths
     * @return a path (e.g. class path) that wraps the libraries
     */
    fun getPath(separator: String = File.pathSeparator): String
}