package eu.iamgio.pikt.explain.data

import eu.iamgio.pikt.explain.SourceImage

/**
 * Converter from raw explanation data to parsed one.
 *
 * @author Giorgio Garofalo
 */
object ExplainDataParser {

    /**
     * @param rawData raw data
     * @return parsed data
     */
    fun parse(rawData: RawExplainData): ExplainData {
        return ExplainData(
                image = this.sourceImage(rawData.sourceImagePath),
                codeLines = this.codeSource(rawData.codeSource).getCodeLines(rawData.code ?: "")
        )
    }

    private fun sourceImage(sourceImagePath: String?): SourceImage {
        return SourceImage() // TODO
    }

    private fun codeSource(codeSource: String?): CodeSource = when(codeSource) {
        "plain" -> PlainTextCodeSource
        // TODO text file path
        else -> PlainTextCodeSource
    }
}