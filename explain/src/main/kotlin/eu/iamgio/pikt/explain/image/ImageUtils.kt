package eu.iamgio.pikt.explain.image

import eu.iamgio.pikt.explain.data.ImageSpecsData

/**
 * Calculates the coordinate of the center of a Pikt pixel within the final image,
 * @param pixel index, either of the column (X) or the row (Y)
 * @return X or Y coordinate of center of the pixel in the final image
 */
fun ImageSpecsData.calcImagePixelCenter(pixel: Int): Int {
    return pixel * this.lineHeight + this.lineHeight / 2
}

/**
 * Calculates the X coordinate of some text within the final image,
 *     horizontally centered, from a given column index.
 * @param pixelX column index
 * @param textWidth width of the rendered text
 * @return X coordinate of the text in the final image
 */
fun ImageSpecsData.calcImageCenteredTextX(pixelX: Int, textWidth: Int): Int {
    return calcImagePixelCenter(pixelX) - textWidth / 2
}

/**
 * Calculates the Y coordinate of some text within the final image,
 *     vertically centered, from a given line index.
 * @param pixelY line index
 * @return Y coordinate of the text in the final image
 */
fun ImageSpecsData.calcImageCenteredTextY(pixelY: Int): Int {
    return calcImagePixelCenter(pixelY) + this.textYOffset
}