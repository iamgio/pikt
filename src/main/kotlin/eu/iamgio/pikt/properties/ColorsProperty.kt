package eu.iamgio.pikt.properties

/**
 * A property value that consists of one or more hexadecimal colors separated by a comma.
 *
 * @param colors list of colors for this property
 * @see ColorsProperties
 * @author Giorgio Garofalo
 */
class ColorsProperty(private val colors: List<String>) {

    /**
     * Checks if a color matches one of those wrapped by this property.
     * @param hex hexadecimal color to check
     * @return whether [colors] contains [hex]
     */
    fun has(hex: String) = hex in colors

    override fun toString() = "ColorsProperty$colors"
}