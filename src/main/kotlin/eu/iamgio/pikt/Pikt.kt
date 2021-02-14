package eu.iamgio.pikt

import eu.iamgio.pikt.properties.PiktPropertiesRetriever

fun main() {
    val properties = PiktPropertiesRetriever().retrieve()
    println(properties)
}