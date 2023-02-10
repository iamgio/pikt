package eu.iamgio.pikt.explain.data

import eu.iamgio.pikt.explain.image.SourceImage
import eu.iamgio.pikt.explain.image.SourceImageFactory

/**
 * Converter from raw explanation data to parsed one.
 *
 * @author Giorgio Garofalo
 */
object ExplainDataParser {

    /**
     * @param rawData raw data
     * @return parsed data
     * @throws IllegalArgumentException if required data is missing
     * @throws IllegalStateException if the image could not be read
     */
    fun parse(rawData: RawExplainData): ExplainData {
        if(rawData.sourceImagePath == null) {
            throw IllegalArgumentException("Source image is missing.")
        }

        val image = this.sourceImage(rawData.sourceImagePath)
                ?: throw IllegalStateException("An error occurred while reading the source image.")

        return ExplainData(
                image,
                codeLines = this.codeSource(rawData.codeSource).getCodeLines(rawData.code ?: "")
        )
    }

    private fun sourceImage(sourceImagePath: String): SourceImage? {
        return SourceImageFactory.fromPath(sourceImagePath)
    }

    private fun codeSource(codeSource: String?): CodeSource = when(codeSource) {
        "plain" -> PlainTextCodeSource
        // TODO text file path
        else -> PlainTextCodeSource
    }
}