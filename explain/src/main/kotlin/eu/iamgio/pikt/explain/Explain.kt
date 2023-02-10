package eu.iamgio.pikt.explain

import eu.iamgio.pikt.explain.data.ExplainDataParser
import eu.iamgio.pikt.explain.data.RawExplainDataSystemPropertiesRetriever
import eu.iamgio.pikt.log.Log

fun main() {
    val data = try {
        ExplainDataParser.parse(RawExplainDataSystemPropertiesRetriever.retrieve())
    } catch(e: Exception) {
        return Log.error(e.message ?: "An error occurred while retrieving data.")
    }

    println(data)
}