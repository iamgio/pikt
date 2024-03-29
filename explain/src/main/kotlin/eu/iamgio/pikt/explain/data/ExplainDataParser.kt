package eu.iamgio.pikt.explain.data

import eu.iamgio.pikt.explain.image.SourceImage
import eu.iamgio.pikt.explain.image.SourceImageFactory
import eu.iamgio.pikt.explain.syntax.*
import java.awt.Color
import java.io.File
import java.io.IOException

/**
 * Converter from raw explanation data to parsed one.
 *
 * @author Giorgio Garofalo
 */
object ExplainDataParser {

    private const val DEFAULT_OUTPUT_NAME = "explain.png"
    private const val DEFAULT_CODE = "" // Human-readable code for explanation.

    /**
     * @param data raw data
     * @return parsed data
     * @throws IllegalArgumentException if required data is missing
     * @throws IOException if the image could not be read
     */
    fun parse(data: RawExplainData): ExplainData {
        if(data.sourceImagePath == null) {
            throw IllegalArgumentException("Source image is missing.")
        }

        val image = this.sourceImage(data.sourceImagePath)
            ?: throw IOException("An error occurred while reading the source image.")

        return ExplainData(
            image,
            output = this.outputImage(data.outputImagePath ?: DEFAULT_OUTPUT_NAME),
            codeLines = this.codeSource(data.code).getCodeLines(data.code ?: DEFAULT_CODE),
            textComments = this.textComments(data.textComments),
            lineComments = this.lineComments(data.lineComments),
            imageSpecs = this.imageSpecs(data),
            syntaxHighlighting = this.syntaxHighlighting(data.syntaxHighlighting)
        )
    }

    private fun sourceImage(sourceImagePath: String): SourceImage? {
        return SourceImageFactory.fromPath(sourceImagePath)
    }

    private fun outputImage(outputImagePath: String): File {
        return File(outputImagePath)
    }

    private fun codeSource(code: String?): CodeSource = when {
        code.isNullOrEmpty() -> EmptyCodeSource
        File(code).exists()  -> FileCodeSource
        else                 -> PlainTextCodeSource
    }

    private fun textComments(comments: Map<Pair<Int, Int>, String>): List<TextCommentData> {
        return comments.map { (position, text) -> TextCommentData(position.first, position.second, text) }
    }

    private fun lineComments(comments: Map<Pair<Int, Int>, Pair<Int, Int>>): List<LineCommentData> {
        return comments.map { (from, to) -> LineCommentData(from.first, from.second, to) }
    }

    private fun parseColor(string: String?): Color? {
        return string?.let { Color.decode(it) }
    }

    private fun imageSpecs(data: RawExplainData): ImageSpecsData = with(ImageSpecsData.Defaults) {
        ImageSpecsData(
            width           = data.imageWidth?.toIntOrNull(),
            backgroundColor = parseColor(data.imageBackgroundColor)      ?: BACKGROUND_COLOR,
            lineHeight      = data.imageLineHeight?.toIntOrNull()        ?: LINE_HEIGHT,
            textColor       = parseColor(data.imageTextColor)            ?: TEXT_COLOR,
            textYOffset     = data.imageTextYOffset?.toIntOrNull()       ?: TEXT_Y_OFFSET,
            fontFamily      = data.imageFontFamily                       ?: FONT_FAMILY,
            fontSize        = data.imageFontSize?.toIntOrNull()          ?: FONT_SIZE,
            separatorColor  = parseColor(data.imageSeparatorColor)       ?: SEPARATOR_COLOR,
            separatorSize   = data.imageSeparatorColor?.toIntOrNull()    ?: SEPARATOR_SIZE,
            commentColor    = parseColor(data.imageCommentColor)         ?: COMMENT_COLOR,
            lineCommentSize = data.imageCommentLineSize?.toFloatOrNull() ?: COMMENT_LINE_SIZE
        )
    }

    private fun syntaxHighlighting(rawSyntaxHighlighting: Map<String, String>): SyntaxHighlighting {
        if(rawSyntaxHighlighting.isEmpty()) {
            return SyntaxHighlightingFactory(DefaultSyntaxHighlightingStyleFactory).default()
        }

        val entries = rawSyntaxHighlighting.asSequence()
            .map { (pattern, color) ->
                val style = SyntaxHighlightingEntryStyle(parseColor(color)!!)
                SyntaxHighlightingEntry(pattern.toRegex(), style)
            }.toList()

        return SyntaxHighlighting(entries)
    }
}