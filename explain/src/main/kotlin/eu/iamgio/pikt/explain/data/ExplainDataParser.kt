package eu.iamgio.pikt.explain.data

import eu.iamgio.pikt.explain.image.SourceImage
import eu.iamgio.pikt.explain.image.SourceImageFactory
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
                codeLines = this.codeSource(data.codeSource).getCodeLines(data.code ?: DEFAULT_CODE),
                imageSpecs = this.imageSpecs(data)
        )
    }

    private fun sourceImage(sourceImagePath: String): SourceImage? {
        return SourceImageFactory.fromPath(sourceImagePath)
    }

    private fun outputImage(outputImagePath: String): File {
        return File(outputImagePath)
    }

    private fun codeSource(codeSource: String?): CodeSource = when(codeSource) {
        "plain" -> PlainTextCodeSource
        // TODO text file path
        else -> PlainTextCodeSource
    }

    private fun parseColor(string: String?): Color? {
        return string?.let { Color.decode(it) }
    }

    private fun imageSpecs(data: RawExplainData): ImageSpecsData = with(ImageSpecsData.Defaults) {
        ImageSpecsData(
                backgroundColor = parseColor(data.imageBackgroundColor) ?: BACKGROUND_COLOR,
                lineHeight = data.imageLineHeight?.toIntOrNull() ?: LINE_HEIGHT,
                textColor = parseColor(data.imageTextColor) ?: TEXT_COLOR,
                fontFamily = data.imageFontFamily ?: FONT_FAMILY,
                fontSize = data.imageFontSize?.toIntOrNull() ?: FONT_SIZE,
                separatorColor = parseColor(data.imageSeparatorColor) ?: SEPARATOR_COLOR,
                separatorSize = data.imageSeparatorColor?.toIntOrNull() ?: SEPARATOR_SIZE,
        )
    }
}