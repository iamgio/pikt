package eu.iamgio.pikt.explain.data

import eu.iamgio.pikt.explain.image.SourceImage
import eu.iamgio.pikt.properties.Properties

/**
 * Data needed for code explanation.
 *
 * @param image source Pikt image
 * @param codeLines lines of explanation code
 * @author Giorgio Garofalo
 */
data class ExplainData(
        val image: SourceImage,
        val codeLines: List<String>
) : Properties