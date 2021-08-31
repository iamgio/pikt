package eu.iamgio.pikt.lib

import eu.iamgio.pikt.properties.ColorsProperty

/**
 * Utility functions for external libraries, including standard library.
 *
 * @author Giorgio Garofalo
 */
object Libraries {

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
        colors = keys
                .filter { it.toString().startsWith(LIBRARY_COLOR_SCHEME_KEY_PREFIX) }
                .associate { it.toString().substring(it.toString().lastIndexOf(".") + 1) to get(it.toString()) }
        return colors
    }

    /**
     * @param hex hexadecimal color to check
     * @return name of the stdlib member linked to [hex] color if exists. `null` otherwise
     */
    fun getMemberName(hex: String): String? {
        return colors.entries.firstOrNull { it.value.has(hex) }?.key
    }
}