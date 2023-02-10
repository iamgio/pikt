package eu.iamgio.pikt.explain.image

import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO

/**
 * Factory for [SourceImage].
 *
 * @author Giorgio Garofalo
 */
object SourceImageFactory {

    /**
     * @param imagePath image file path
     * @return image from file path, or `null` if an error occurred
     */
    fun fromPath(imagePath: String): SourceImage? {
        return this.fromInputStream(FileInputStream(imagePath))
    }

    /**
     * @param inputStream image input stream
     * @return image from input stream, or `null` if an error occurred
     */
    private fun fromInputStream(inputStream: InputStream): SourceImage? {
        return try {
            SourceImage(ImageIO.read(inputStream))
        } catch(e: IOException) {
            null
        }
    }
}