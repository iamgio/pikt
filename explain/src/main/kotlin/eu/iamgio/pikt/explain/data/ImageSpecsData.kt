package eu.iamgio.pikt.explain.data

import eu.iamgio.pikt.properties.Properties
import java.awt.Color
import java.awt.Font

/**
 * Values that change the look of the output image.
 *
 * @param width optional width of the image
 * @param backgroundColor background color of the image
 * @param lineHeight height (scale) of a pixel of the source image
 * @param textColor default code text color
 * @param fontFamily font family of the code text, either as a name or as a path to its file
 * @param fontSize font size of the code text
 * @param separatorColor color of the line separators
 * @param separatorSize height of the line separators
 * @author Giorgio Garofalo
 */
data class ImageSpecsData(
    val width: Int?,
    val backgroundColor: Color,
    val lineHeight: Int,
    val textColor: Color,
    val fontFamily: String,
    val fontSize: Int,
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
         * Default value for [ImageSpecsData.textColor]
         */
        val TEXT_COLOR: Color = Color.WHITE

        /**
         * Default value for [ImageSpecsData.fontFamily]
         */
        const val FONT_FAMILY = Font.MONOSPACED

        /**
         * Default value for [ImageSpecsData.fontSize]
         */
        const val FONT_SIZE = 18

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