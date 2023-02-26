package eu.iamgio.pikt.explain

import eu.iamgio.pikt.explain.data.ExplainDataParser
import eu.iamgio.pikt.explain.data.RawExplainDataSystemPropertiesRetriever
import eu.iamgio.pikt.explain.image.ExplanationImage
import eu.iamgio.pikt.explain.image.PixelPerfectImageScaling
import eu.iamgio.pikt.log.Log
import javax.imageio.ImageIO

fun main() {
    try {
        // Accessing core's classes to check classpath.
        Log.debug("Started")
    } catch(e: NoClassDefFoundError) {
        return System.err.println("Could not find pikt.jar in this directory.")
    }

    val data = try {
        ExplainDataParser.parse(RawExplainDataSystemPropertiesRetriever.retrieve())
    } catch(e: Exception) {
        return Log.error(e.message ?: "An error occurred while retrieving data.")
    }

    val scaled = data.image.scale(PixelPerfectImageScaling, factor = data.imageSpecs.lineHeight)
    val output = ExplanationImage(scaled, data.codeLines, data.comments, data.syntaxHighlighting, data.imageSpecs)

    ImageIO.write(output.generate(), data.output.extension, data.output)
}