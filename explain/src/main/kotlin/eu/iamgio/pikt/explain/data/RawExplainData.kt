package eu.iamgio.pikt.explain.data

import eu.iamgio.pikt.properties.Properties
import eu.iamgio.pikt.properties.PropertiesRetriever

/**
 * Raw data, possibly retrieved from system properties,
 *  to be turned to parsed [ExplainData].
 *
 * @param sourceImagePath path to the source Pikt image
 * @param codeSource strategy used to get code
 * @param code text code. Its parsing depends on the [codeSource]
 * @author Giorgio Garofalo
 */
data class RawExplainData(
        val sourceImagePath: String?,
        val outputImagePath: String?,
        val codeSource: String?,
        val code: String?,
        val imageBackgroundColor: String?,
        val imageLineHeight: String?,
) : Properties

/**
 * Retriever of raw data from system properties.
 *
 * @author Giorgio Garofalo
 */
object RawExplainDataSystemPropertiesRetriever : PropertiesRetriever<RawExplainData> {

    private const val PROPERTY_SOURCE_IMAGE = "source"
    private const val PROPERTY_OUTPUT_IMAGE = "out"
    private const val PROPERTY_CODE_SOURCE = "codesrc"
    private const val PROPERTY_CODE = "code"
    private const val PROPERTY_BACKGROUND_COLOR = "imgbg"
    private const val PROPERTY_LINE_HEIGHT = "imglineheight"

    override fun retrieve() = RawExplainData(
            sourceImagePath = System.getProperty(PROPERTY_SOURCE_IMAGE),
            outputImagePath = System.getProperty(PROPERTY_OUTPUT_IMAGE),
            codeSource = System.getProperty(PROPERTY_CODE_SOURCE),
            code = System.getProperty(PROPERTY_CODE),
            imageBackgroundColor = System.getProperty(PROPERTY_BACKGROUND_COLOR),
            imageLineHeight = System.getProperty(PROPERTY_LINE_HEIGHT)
    )
}