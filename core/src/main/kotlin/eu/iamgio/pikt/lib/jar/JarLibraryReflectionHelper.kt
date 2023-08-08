package eu.iamgio.pikt.lib.jar

import java.lang.reflect.Method

/**
 * A helper that allows accessing members from a JAR library via reflection.
 *
 * @param library corresponding JAR library
 * @author Giorgio Garofalo
 */
class JarLibraryReflectionHelper(private val library: JarLibrary) {

    /**
     * Reads a JVM [Class] from the external [library].
     * @param className name of the class to read.
     */
    private fun getClass(className: String): Class<*> = Class.forName(className, true, library.classLoader)

    /**
     * @return a list of the methods within the library
     */
    fun getAllMethods(): List<Method> {
        val methods = mutableListOf<Method>()
        library.getClasses().forEach {
            methods.addAll(getClass(it).methods)
        }
        return methods
    }
}