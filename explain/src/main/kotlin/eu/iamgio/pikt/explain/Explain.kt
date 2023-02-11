package eu.iamgio.pikt.explain

import eu.iamgio.pikt.explain.data.ExplainDataParser
import eu.iamgio.pikt.explain.data.RawExplainDataSystemPropertiesRetriever
import eu.iamgio.pikt.explain.image.ExplanationImage
import eu.iamgio.pikt.explain.image.PixelPerfectImageScaling
import eu.iamgio.pikt.log.Log
import javax.imageio.ImageIO

fun main() {
    val data = try {
        ExplainDataParser.parse(RawExplainDataSystemPropertiesRetriever.retrieve())
    } catch(e: Exception) {
        return Log.error(e.message ?: "An error occurred while retrieving data.")
    }

    val scaled = data.image.scale(PixelPerfectImageScaling, factor = data.imageSpecs.lineHeight)
    val output = ExplanationImage(scaled, data.codeLines, data.imageSpecs)

    ImageIO.write(output.generate(), data.output.extension, data.output)
}