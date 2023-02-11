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
     * @param rawData raw data
     * @return parsed data
     * @throws IllegalArgumentException if required data is missing
     * @throws IOException if the image could not be read
     */
    fun parse(rawData: RawExplainData): ExplainData {
        if(rawData.sourceImagePath == null) {
            throw IllegalArgumentException("Source image is missing.")
        }

        val image = this.sourceImage(rawData.sourceImagePath)
                ?: throw IOException("An error occurred while reading the source image.")

        return ExplainData(
                image,
                output = this.outputImage(rawData.outputImagePath ?: DEFAULT_OUTPUT_NAME),
                codeLines = this.codeSource(rawData.codeSource).getCodeLines(rawData.code ?: DEFAULT_CODE),
                imageSpecs = this.imageSpecs(
                        rawData.imageBackgroundColor,
                        rawData.imageLineHeight,
                        rawData.imageSeparatorColor,
                        rawData.imageSeparatorSize
                )
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

    private fun imageSpecs(
            backgroundColor: String?,
            lineHeight: String?,
            separatorColor: String?,
            separatorSize: String?
    ): ImageSpecsData = with(ImageSpecsData.Defaults) {
        ImageSpecsData(
                backgroundColor = parseColor(backgroundColor) ?: BACKGROUND_COLOR,
                lineHeight = lineHeight?.toIntOrNull() ?: LINE_HEIGHT,
                separatorColor = parseColor(separatorColor) ?: SEPARATOR_COLOR,
                separatorSize = separatorSize?.toIntOrNull() ?: SEPARATOR_SIZE
        )
    }
}