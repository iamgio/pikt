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
 * @param syntaxHighlighting syntax highlighting rules as pattern-color pairs
 * @param imageWidth optional width of the output image
 * @param imageBackgroundColor background color of the output image
 * @param imageLineHeight height of a pixel from the source image
 * @param imageTextColor default code text color
 * @param imageFontFamily font family of code text, either as its name or as a path to its file
 * @param imageFontSize font size of code text
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
    val syntaxHighlighting: Map<String, String>?,
    val imageWidth: String?,
    val imageBackgroundColor: String?,
    val imageLineHeight: String?,
    val imageTextColor: String?,
    val imageFontFamily: String?,
    val imageFontSize: String?,
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
    private const val PROPERTY_IMAGE_WIDTH = "imgwidth"
    private const val PROPERTY_BACKGROUND_COLOR = "imgbg"
    private const val PROPERTY_LINE_HEIGHT = "imglineheight"
    private const val PROPERTY_TEXT_COLOR = "imgtextcolor"
    private const val PROPERTY_FONT_FAMILY = "imgfontfamily"
    private const val PROPERTY_FONT_SIZE = "imgfontsize"
    private const val PROPERTY_SEPARATOR_COLOR = "imgseparatorcolor"
    private const val PROPERTY_SEPARATOR_SIZE = "imgseparatorsize"

    // Syntax highlighting is defined via properties that begins with a prefix:
    // -Dsyntax::MYREGEX=MYCOLOR
    private const val SYNTAX_HIGHLIGHTING_PROPERTY_PREFIX = "syntax::"

    // '=' can't be used in system property keys, so '~~' (double tilde) can be used as a substitute.
    private const val SYNTAX_HIGHLIGHTING_EQUALS_SUBSTITUTE = "~~"
    private const val SYNTAX_HIGHLIGHTING_EQUALS_REPLACEMENT = "="

    override fun retrieve() = RawExplainData(
        sourceImagePath = System.getProperty(PROPERTY_SOURCE_IMAGE),
        outputImagePath = System.getProperty(PROPERTY_OUTPUT_IMAGE),
        codeSource = System.getProperty(PROPERTY_CODE_SOURCE),
        code = System.getProperty(PROPERTY_CODE),
        syntaxHighlighting = this.retrieveRawSyntaxHighlighting(),
        imageWidth = System.getProperty(PROPERTY_IMAGE_WIDTH),
        imageBackgroundColor = System.getProperty(PROPERTY_BACKGROUND_COLOR),
        imageLineHeight = System.getProperty(PROPERTY_LINE_HEIGHT),
        imageTextColor = System.getProperty(PROPERTY_TEXT_COLOR),
        imageFontFamily = System.getProperty(PROPERTY_FONT_FAMILY),
        imageFontSize = System.getProperty(PROPERTY_FONT_SIZE),
        imageSeparatorColor = System.getProperty(PROPERTY_SEPARATOR_COLOR),
        imageSeparatorSize = System.getProperty(PROPERTY_SEPARATOR_SIZE)
    )

    /**
     * Converts syntax highlighting properties to pattern-color pairs.
     * @return syntax highlighting rules as a raw pattern-color map.
     *  For instance, `-Dsyntax::MYREGEX=MYCOLOR` is turned into a `MYREGEX-MYCOLOR` pair
     */
    private fun retrieveRawSyntaxHighlighting(): Map<String, String>? {
        return System.getProperties()?.asSequence()
            // Only get selected properties that begin with the given prefix.
            ?.filter { it.key.toString().startsWith(SYNTAX_HIGHLIGHTING_PROPERTY_PREFIX) }
            ?.map { (pattern, color) ->
                pattern.toString()
                    // Replace equals sign '=' with a supported alternative.
                    .replace(SYNTAX_HIGHLIGHTING_EQUALS_SUBSTITUTE, SYNTAX_HIGHLIGHTING_EQUALS_REPLACEMENT)
                    // Remove prefix.
                    .substring(SYNTAX_HIGHLIGHTING_PROPERTY_PREFIX.length) to color.toString()
            }
            ?.toMap()
    }
}