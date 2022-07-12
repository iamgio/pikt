package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.readImage
import eu.iamgio.pikt.image.save
import java.awt.image.BufferedImage
import java.io.File

// This file contains commands and utilities regarding image processing, which takes a source image and gives another image out
// through several modifications and manipulations (color schemes, size, etc.).

object ImageProcessingUtils {

    /**
     * Processed image output. [getOutputFileFromSource] value will be used by default if this is `null`.
     * @see ImageOutputCommand
     */
    var output: File? = null

    /**
     * Enables image output chaining: the output of an image processing command becomes the input for the next command.
     * @see ChainOutputCommand
     */
    var enableChaining: Boolean = false

    /**
     * Gets the default output file. E.g.: hello_world.png -> hello_world_tag.png
     * @param source source image that is processed
     * @param tag suffix of the file name representing the operation executed.
     * @return default output file.
     */
    private fun getOutputFileFromSource(source: File, tag: String) = File(source.parentFile, source.nameWithoutExtension + "_$tag." + source.extension)

    fun read(imageFile: File) = readImage(output?.takeIf { enableChaining } ?: imageFile)

    /**
     * Saves the given [image] to a file.
     * @param image image to save
     * @param source source image, used for [getOutputFileFromSource] in case [output] is `null`
     * @param tag suffix of the file name, used for [getOutputFileFromSource] in case [output] is `null`
     * @return output file
     */
    fun save(image: BufferedImage, source: File, tag: String): File {
        val out = output ?: getOutputFileFromSource(source, tag)
        image.save(out)
        return out
    }
}

/**
 * Triggered by -imgoutput=path argument.
 * Defines the output file name for the operations in this file.
 *
 * @see ImageProcessingUtils.output
 * @author Giorgio Garofalo
 */
class ImageOutputCommand : Command("-imgoutput", { args ->
    if(args == null) {
        System.err.println("Expected -imgoutput=<path>. Using default path.")
    } else if(ImageProcessingUtils.output == null) {
        ImageProcessingUtils.output = File(args)
    }
}, isSettingsCommand = true)

/**
 * Triggered by -chainoutput argument.
 * Allows chaining between image transformation commands: the output of an image processing command becomes the input for the next command.
 * It is recommended to use in combination with [ImageOutputCommand].
 *
 * @see ImageProcessingUtils.enableChaining
 * @author Giorgio Garofalo
 */
class ChainOutputCommand : Command("-chainoutput", {
    ImageProcessingUtils.enableChaining = true
}, isSettingsCommand = true)