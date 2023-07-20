package eu.iamgio.pikt.eval

import eu.iamgio.pikt.properties.PiktProperties

/**
 * Values that are injected into the output code.
 *
 * @param sourceImagePath absolute path of the source file
 */
data class InjectionData(
    val sourceImagePath: String
) {
    companion object {

        /**
         * @return a new instance of [InjectionData] loaded with information from Pikt [properties]
         * @param properties Pikt properties to extract data from
         */
        fun fromProperties(properties: PiktProperties) = InjectionData(
            sourceImagePath = properties.source.absolutePath
        )
    }
}
