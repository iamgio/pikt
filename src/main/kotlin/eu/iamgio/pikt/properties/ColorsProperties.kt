package eu.iamgio.pikt.properties

import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * Storage for properties, loaded from a .properties file, that define the "keywords" of the language.
 * A standard file can be created by running Pikt with the -createcolors <name> argument.
 * The fields of this class refer to a hexadecimal color.
 *
 * @param variable define/set variables
 * @author Giorgio Garofalo
 */
data class ColorsProperties(
        val variable: String
) : Properties

/**
 * Class that parses JVM properties into a [PiktProperties] instance.
 *
 * @author Giorgio Garofalo
 */
class ColorsPropertiesRetriever : PropertiesRetriever<ColorsProperties> {

    /**
     * External properties
     */
    private val properties = java.util.Properties()

    /**
     * Internal properties used to fill missing properties
     */
    private val internalProperties = java.util.Properties()

    /**
     * Loads external properties
     * @param propertiesPath path to the .properties file
     */
    fun loadProperties(propertiesPath: String) {
        properties.load(FileInputStream(propertiesPath))
    }

    /**
     * Gets the value paired with [key] from the external [properties]. If the value is missing, it gets it from [internalProperties].
     * @return corresponding hex value
     */
    fun get(key: String): String {
        return if(key in properties) {
            properties.getProperty(key)
        } else {
            internalProperties.getProperty(key)
        }
    }

    /**
     * Converts values specified by a .properties file to parsed [ColorsProperties].
     * @return parsed properties
     */
    override fun retrieve(): ColorsProperties {
        internalProperties.load(InputStreamReader(javaClass.getResourceAsStream("/properties/colors.properties")))

        return ColorsProperties(
                get("variable")
        )
    }
}