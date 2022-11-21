package eu.iamgio.pikt.lib

import eu.iamgio.pikt.properties.ColorsProperty

/**
 * A bridge between library symbols (Kotlin) and their colors.
 *
 * @author Giorgio Garofalo
 */
class LibrariesColors {

    /**
     * Stored name=colors map, initialized after [withGeneratedColorProperties] is called.
     */
    private lateinit var colors: Map<String, ColorsProperty>

    /**
     * Generates a name=hex map for standard library members and stores it into [colors].
     * @param keys color properties keys
     * @param get function getting color value from key
     * @return `this`
     */
    fun withGeneratedColorProperties(keys: Set<Any>, get: (String) -> ColorsProperty): LibrariesColors {
        colors = generateColorPropertiesForSubsection(keys, LIBRARY_COLOR_SCHEME_KEY_PREFIX, get)
        return this
    }

    /**
     * @param hex hexadecimal color to check
     * @return name of the library member linked to [hex] color if it exists, `null` otherwise
     */
    fun getMemberName(hex: String): String? {
        return colors.entries.firstOrNull { it.value.has(hex) }?.key
    }

    /**
     * @param name library member name to check
     * @return colors of the library member linked saved as [name] if it exists, `null` otherwise
     */
    fun getColorsFor(name: String): ColorsProperty? {
        return colors.entries.firstOrNull { it.key == name }?.value
    }

    /**
     * Generates a name=colors map for a given properties sub-section specified by [prefix].
     *
     * Example:
     * ```
     * section.property1=colors1
     * section.property2=colors2
     * ```
     *
     * This function called with `prefix` = `section`
     * generates:
     *
     * `{property1=colors1, property2=colors2}`
     *
     * @param keys color properties keys
     * @param prefix sub-section prefix
     * @param get function getting color value from key
     * @return standard library color scheme
     */
    private fun generateColorPropertiesForSubsection(keys: Set<Any>, prefix: String, get: (String) -> ColorsProperty): Map<String, ColorsProperty> {
        return keys
            .filter { it.toString().startsWith(prefix) }
            .associate {
                it.toString().substring(it.toString().lastIndexOf(".") + 1) to get(it.toString())
            }
    }
}