package eu.iamgio.pikt.explain

import eu.iamgio.pikt.explain.data.ExplainDataParser
import eu.iamgio.pikt.explain.data.RawExplainDataSystemPropertiesRetriever

fun main() {
    val data = ExplainDataParser.parse(RawExplainDataSystemPropertiesRetriever.retrieve())
    println(data)
}