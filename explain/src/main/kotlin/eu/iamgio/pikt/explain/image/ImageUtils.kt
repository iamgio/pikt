package eu.iamgio.pikt.explain.image

import eu.iamgio.pikt.explain.data.ImageSpecsData

/**
 * Calculates the X coordinate of some text within the final image,
 *     horizontally centered, from a given column index.
 * @param pixelX column index
 * @param textWidth width of the rendered text
 * @return X coordinate of the text in the final image
 */
fun ImageSpecsData.calcImageCenteredTextX(pixelX: Int, textWidth: Int): Int {
    return pixelX * this.lineHeight + (this.lineHeight - textWidth) / 2
}

/**
 * Calculates the Y coordinate of some text within the final image,
 *     vertically centered, from a given line index.
 * @param pixelY line index
 * @return Y coordinate of the text in the final image
 */
fun ImageSpecsData.calcImageCenteredTextY(pixelY: Int): Int {
    return pixelY * this.lineHeight + this.lineHeight / 2 + this.textYOffset
}