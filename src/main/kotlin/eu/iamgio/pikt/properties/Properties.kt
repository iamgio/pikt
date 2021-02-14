package eu.iamgio.pikt.properties

/**
 * Inherited by data classes containing immutable properties.
 *
 * @author Giorgio Garofalo
 */
interface Properties

/**
 * Used to generate an immutable [Properties] class.
 *
 * @author Giorgio Garofalo
 */
interface PropertiesRetriever<out T : Properties> {

    /**
     * Parses and generates properties.
     * @return generated [Properties]
     */
    fun retrieve(): T
}