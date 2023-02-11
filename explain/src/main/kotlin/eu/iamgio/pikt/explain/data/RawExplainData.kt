package eu.iamgio.pikt.explain.data

import eu.iamgio.pikt.properties.Properties
import eu.iamgio.pikt.properties.PropertiesRetriever

/**
 * Raw data, possibly retrieved from system properties,
 *  to be turned to parsed [ExplainData].
 *
 * @param sourceImagePath path to the source Pikt image
 * @param outputImagePath path to the output image file
 * @param codeSource strategy used to get code
 * @param code text code. Its parsing depends on the [codeSource]
 * @param imageBackgroundColor background color of the output image
 * @param imageLineHeight height of a pixel from the source image
 * @param imageSeparatorColor color of the line separators
 * @param imageSeparatorSize height of the line separators
 * @see ExplainData
 * @see ImageSpecsData
 * @author Giorgio Garofalo
 */
data class RawExplainData(
        val sourceImagePath: String?,
        val outputImagePath: String?,
        val codeSource: String?,
        val code: String?,
        val imageBackgroundColor: String?,
        val imageLineHeight: String?,
        val imageSeparatorColor: String?,
        val imageSeparatorSize: String?
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
    private const val PROPERTY_SEPARATOR_COLOR = "imgseparatorcolor"
    private const val PROPERTY_SEPARATOR_SIZE = "imgseparatorsize"

    override fun retrieve() = RawExplainData(
            sourceImagePath = System.getProperty(PROPERTY_SOURCE_IMAGE),
            outputImagePath = System.getProperty(PROPERTY_OUTPUT_IMAGE),
            codeSource = System.getProperty(PROPERTY_CODE_SOURCE),
            code = System.getProperty(PROPERTY_CODE),
            imageBackgroundColor = System.getProperty(PROPERTY_BACKGROUND_COLOR),
            imageLineHeight = System.getProperty(PROPERTY_LINE_HEIGHT),
            imageSeparatorColor = System.getProperty(PROPERTY_SEPARATOR_COLOR),
            imageSeparatorSize = System.getProperty(PROPERTY_SEPARATOR_SIZE)
    )
}