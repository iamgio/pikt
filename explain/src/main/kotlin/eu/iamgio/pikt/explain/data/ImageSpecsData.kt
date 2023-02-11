package eu.iamgio.pikt.explain.data

import eu.iamgio.pikt.properties.Properties
import java.awt.Color

/**
 * Values that change the look of the output image.
 * 
 * @param backgroundColor background color of the image
 * @param lineHeight height (scale) of a pixel of the source image
 * @param separatorColor color of the line separators
 * @param separatorSize height of the line separators
 * @author Giorgio Garofalo
 */
data class ImageSpecsData(
        val backgroundColor: Color,
        val lineHeight: Int,
        val separatorColor: Color,
        val separatorSize: Int,
) : Properties {

    /**
     * Default values for [ImageSpecsData].
     */
    object Defaults {

        /**
         * Default value for [ImageSpecsData.backgroundColor].
         */
        val BACKGROUND_COLOR: Color = Color.BLACK

        /**
         * Default value for [ImageSpecsData.lineHeight].
         */
        const val LINE_HEIGHT = 30

        /**
         * Default value for [ImageSpecsData.separatorColor].
         */
        val SEPARATOR_COLOR: Color = Color(1F, 1F, 1F, .1F)

        /**
         * Default value for [ImageSpecsData.separatorSize].
         */
        const val SEPARATOR_SIZE = 2
    }
}