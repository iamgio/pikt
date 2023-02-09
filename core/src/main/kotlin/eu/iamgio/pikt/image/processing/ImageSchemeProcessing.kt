package eu.iamgio.pikt.image.processing

import eu.iamgio.pikt.lib.Libraries
import eu.iamgio.pikt.properties.ColorsProperty
import eu.iamgio.pikt.properties.INTERNAL_COLORS_SCHEME_PATH
import java.awt.image.BufferedImage
import java.util.*

/**
 * Implemented by manipulations that execute operations on an image and return another one.
 */
interface ImageProcessing {
    /**
     * Runs the process.
     */
    fun process(): BufferedImage
}

/**
 * Class that allows operations between an image, its custom scheme and the default internal scheme.
 *
 * @param customScheme custom color scheme to handle
 * @author Giorgio Garofalo
 */
sealed class ImageSchemeProcessing(
    protected val image: BufferedImage,
    private val customScheme: Properties,
    private val libraries: Libraries
) : ImageProcessing {

    /**
     * Wrapper of internal and custom color schemes.
     *
     * @param internal internal default scheme
     * @param custom custom scheme
     */
    data class SchemesPair(val internal: Map<String, ColorsProperty>, val custom: Map<String, ColorsProperty>)

    /**
     * @return color scheme property keys associated to their color values
     */
    private fun Properties.asMap(): Map<String, ColorsProperty> {
        return keys.associate { it.toString() to ColorsProperty.of(getProperty(it.toString())) }
    }

    /**
     * @return the schemes as a pair of two maps: the internal one and the custom one
     */
    protected fun retrieveSchemes(): SchemesPair {
        val internalScheme = Properties().also { it.load(javaClass.getResourceAsStream(INTERNAL_COLORS_SCHEME_PATH)!!) }

        // Load library colors
        libraries.forEach {
            it.colorScheme?.properties?.asMap()?.forEach { (key, colors) ->
                internalScheme.setProperty(it.info.getFullKey(key), colors.stringify())
            }
        }

        return SchemesPair(internalScheme.asMap(), customScheme.asMap())
    }
}