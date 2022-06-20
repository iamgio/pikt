package eu.iamgio.pikt.schemes

import eu.iamgio.pikt.properties.ColorsProperty

private const val REPLACEMENT_COLOR_SCHEME_PREFIX = "change."

/**
 * Utility class that handles color replacements, used in [ImageSchemeProcessing]s.
 *
 * For example, a color scheme containing:
 * ```
 * change.FF0000=FFFF00
 * change.0000FF,00FFFF=00FF00,FF00FF
 * ```
 *
 * Will:
 * 1. Replace `#FF0000` with `#FFFF00`
 * 2. Replace `#0000FF` and `#00FFFF` with either `#00FF00` or `#FF00FF` (depending on the choice method used)
 *
 * @author Giorgio Garofalo
 */
object ColorReplacements {
    /**
     * Generates a colors=colors map for color replacements. In a scheme they are defined via `change.from_hex=to_hex`.
     * @param keys color properties keys
     * @param get function getting color value from key
     * @return replacements color scheme
     */
    fun generateColorProperties(keys: Set<Any>, get: (String) -> ColorsProperty): Map<ColorsProperty, ColorsProperty> {
        return ColorsProperty.generateColorPropertiesForSubsection(keys, REPLACEMENT_COLOR_SCHEME_PREFIX, get)
                .mapKeys { ColorsProperty.of(it.key) }
    }
}