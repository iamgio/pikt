package eu.iamgio.pikt.lib

import java.util.*

/**
 * Library-specific options specified by a .properties file.
 *
 * @param `package` package to import
 * @param prefix color scheme prefix
 * @author Giorgio Garofalo
 */
data class LibraryInfo(
        val `package`: String,
        val prefix: String
) {
    /**
     * @param key property key
     * @return full property key: <global prefix>.<library prefix>.<key>
     */
    fun getFullKey(key: String) = "$LIBRARY_COLOR_SCHEME_KEY_PREFIX${prefix}.$key"

    companion object {
        /**
         * Instantiates a [LibraryInfo] from a .properties file.
         * @param properties Java Properties containing the library information
         * @return parsed library information
         */
        fun fromProperties(properties: Properties) = LibraryInfo(
                properties.getProperty("package"),
                properties.getProperty("prefix")
        )
    }
}