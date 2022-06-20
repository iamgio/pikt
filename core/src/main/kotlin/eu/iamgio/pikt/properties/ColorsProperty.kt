package eu.iamgio.pikt.properties

/**
 * Separates multiple colors of the same property in schemes.
 */
const val COLORS_SEPARATOR = ","

/**
 * A property value that consists of one or more hexadecimal colors separated by a comma.
 *
 * @param colors list of colors for this property
 * @see ColorsProperties
 * @author Giorgio Garofalo
 */
data class ColorsProperty(val colors: List<String>) {

    /**
     * Checks if a color matches one of those wrapped by this property.
     * @param hex hexadecimal color to check
     * @return whether [colors] contains [hex]
     */
    fun has(hex: String) = hex in colors

    /**
     * Unconverts [colors] back to a string divided by commas.
     * @return [colors] as a string
     */
    fun stringify() = colors.joinToString(separator = ",")

    companion object {

        /**
         * @param raw property string
         * @return raw string property split into a [ColorsProperty]
         */
        fun of(raw: String) = ColorsProperty(raw.split(COLORS_SEPARATOR))

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
        fun generateColorPropertiesForSubsection(keys: Set<Any>, prefix: String, get: (String) -> ColorsProperty): Map<String, ColorsProperty> {
            return keys
                    .filter { it.toString().startsWith(prefix) }
                    .associate {
                        it.toString().substring(it.toString().lastIndexOf(prefix) + prefix.length) to get(it.toString())
                    }
        }
    }
}