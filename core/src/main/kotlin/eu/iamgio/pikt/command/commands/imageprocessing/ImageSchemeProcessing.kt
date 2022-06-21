package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.lib.JarLibrary
import eu.iamgio.pikt.properties.ColorsProperty
import eu.iamgio.pikt.properties.INTERNAL_COLORS_SCHEME_PATH
import java.awt.image.BufferedImage
import java.util.*

/**
 * Implemented by commands that execute operations on an image and return another one.
 */
interface ImageProcessing {
    /**
     * Runs the process.
     */
    fun process(): BufferedImage
}

/**
 * Class that allows operations between an image, its custom scheme and the default internal scheme.
 * @param customScheme custom colors scheme to handle
 * @author Giorgio Garofalo
 */
sealed class ImageSchemeProcessing(protected val image: BufferedImage, private val customScheme: Properties, private val libraries: List<JarLibrary>) : ImageProcessing {

    /**
     * Wrapper of internal and custom color schemes.
     *
     * @param internal internal default scheme
     * @param custom custom scheme
     */
    data class SchemesPair(val internal: Map<String, ColorsProperty>, val custom: Map<String, ColorsProperty>)

    /**
     * @return the schemes as a pair of two maps: the internal one and the custom one
     */
    protected fun retrieveSchemes(): SchemesPair {
        fun Properties.asMap() = keys.associate { it.toString() to ColorsProperty.of(getProperty(it.toString())) }
        val internalScheme = Properties().also { it.load(javaClass.getResourceAsStream(INTERNAL_COLORS_SCHEME_PATH)!!) }
        libraries.forEach {
            it.colorScheme?.properties?.asMap()?.forEach { (key, colors) ->
                internalScheme.setProperty(it.info.getFullKey(key), colors.stringify())
            }
        }
        return SchemesPair(internalScheme.asMap(), customScheme.asMap())
    }
}