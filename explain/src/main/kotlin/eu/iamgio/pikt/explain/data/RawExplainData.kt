package eu.iamgio.pikt.explain.data

import eu.iamgio.pikt.properties.Properties
import eu.iamgio.pikt.properties.PropertiesRetriever

/**
 * Raw data, possibly retrieved from system properties,
 *  to be turned to parsed [ExplainData].
 *
 * @param sourceImagePath path to the source Pikt image
 * @param outputImagePath path to the output image file
 * @param code text code or path to code file
 * @param comments comments on the source image as a (x-y)-text map
 * @param syntaxHighlighting syntax highlighting rules as pattern-color pairs
 * @param imageWidth optional width of the output image
 * @param imageBackgroundColor background color of the output image
 * @param imageLineHeight height of a pixel from the source image
 * @param imageTextColor default code text color
 * @param imageTextYOffset Y offset of the text to help aligning it
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
    val code: String?,
    val comments: Map<Pair<Int, Int>, String>,
    val syntaxHighlighting: Map<String, String>,
    val imageWidth: String?,
    val imageBackgroundColor: String?,
    val imageLineHeight: String?,
    val imageTextColor: String?,
    val imageTextYOffset: String?,
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
    private const val PROPERTY_CODE = "code"
    private const val PROPERTY_IMAGE_WIDTH = "imgwidth"
    private const val PROPERTY_BACKGROUND_COLOR = "imgbg"
    private const val PROPERTY_LINE_HEIGHT = "imglineheight"
    private const val PROPERTY_TEXT_COLOR = "imgtextcolor"
    private const val PROPERTY_TEXT_Y_OFFSET = "imgtextyoffset"
    private const val PROPERTY_FONT_FAMILY = "imgfontfamily"
    private const val PROPERTY_FONT_SIZE = "imgfontsize"
    private const val PROPERTY_SEPARATOR_COLOR = "imgseparatorcolor"
    private const val PROPERTY_SEPARATOR_SIZE = "imgseparatorsize"

    // A code comment is defined via a property that begins with a prefix:
    // -Dcomment::X.Y=TEXT
    private const val COMMENT_PROPERTY_PREFIX = "comment::"

    // Separator of a comment's X and Y coordinates.
    private const val COMMENT_COORDINATES_SEPARATOR = "."

    // Syntax highlighting is defined via properties that begins with a prefix:
    // -Dsyntax::MYREGEX=MYCOLOR
    private const val SYNTAX_HIGHLIGHTING_PROPERTY_PREFIX = "syntax::"

    // '=' can't be used in system property keys, so '~~' (double tilde) can be used as a substitute.
    private const val SYNTAX_HIGHLIGHTING_EQUALS_SUBSTITUTE = "~~"
    private const val SYNTAX_HIGHLIGHTING_EQUALS_REPLACEMENT = "="

    override fun retrieve() = RawExplainData(
        sourceImagePath = System.getProperty(PROPERTY_SOURCE_IMAGE),
        outputImagePath = System.getProperty(PROPERTY_OUTPUT_IMAGE),
        code = System.getProperty(PROPERTY_CODE),
        comments = this.retrieveComments(),
        syntaxHighlighting = this.retrieveRawSyntaxHighlighting(),
        imageWidth = System.getProperty(PROPERTY_IMAGE_WIDTH),
        imageBackgroundColor = System.getProperty(PROPERTY_BACKGROUND_COLOR),
        imageLineHeight = System.getProperty(PROPERTY_LINE_HEIGHT),
        imageTextColor = System.getProperty(PROPERTY_TEXT_COLOR),
        imageTextYOffset = System.getProperty(PROPERTY_TEXT_Y_OFFSET),
        imageFontFamily = System.getProperty(PROPERTY_FONT_FAMILY),
        imageFontSize = System.getProperty(PROPERTY_FONT_SIZE),
        imageSeparatorColor = System.getProperty(PROPERTY_SEPARATOR_COLOR),
        imageSeparatorSize = System.getProperty(PROPERTY_SEPARATOR_SIZE)
    )

    /**
     * @return system properties that begin with the given [prefix] as (key, value) pairs
     */
    private fun getPropertiesFromPrefix(prefix: String): Sequence<Pair<String, String>> {
        return System.getProperties().asSequence()
            .filter { it.key.toString().startsWith(prefix) }
            .map { it.key.toString().substring(prefix.length) to it.value.toString() }
    }

    /**
     * Converts syntax highlighting properties to pattern-color pairs.
     * @return syntax highlighting rules as a raw pattern-color map.
     *  For instance, `-Dsyntax::MYREGEX=MYCOLOR` is turned into a `MYREGEX-MYCOLOR` pair
     */
    private fun retrieveRawSyntaxHighlighting(): Map<String, String> {
        return getPropertiesFromPrefix(SYNTAX_HIGHLIGHTING_PROPERTY_PREFIX)
            .map { (pattern, color) ->
                // Replace equals sign '=' with a supported alternative.
                pattern.replace(SYNTAX_HIGHLIGHTING_EQUALS_SUBSTITUTE, SYNTAX_HIGHLIGHTING_EQUALS_REPLACEMENT) to color
            }.toMap()
    }

    /**
     * Converts comment properties to position-text pairs.
     * @return image comments as a (x-y)-text map.
     */
    private fun retrieveComments(): Map<Pair<Int, Int>, String> {
        return getPropertiesFromPrefix(COMMENT_PROPERTY_PREFIX)
            .map { (position, text) -> position.split(COMMENT_COORDINATES_SEPARATOR) to text }
            .map { (coords, text) -> coords.map { it.toInt() } to text }
            .map { (coords, text) ->
                val x = coords.firstOrNull() ?: 0
                val y = coords.elementAtOrNull(1) ?: 0
                (x to y) to text
            }.toMap()
    }
}