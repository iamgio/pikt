package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.schemes.RecolorizeImageProcessing
import eu.iamgio.pikt.schemes.StandardizeImageProcessing
import java.awt.image.BufferedImage
import java.io.File

// This file contains commands and utilities regarding image processing, which takes a source image and gives another image out
// through several modifications and manipulations (color schemes, size, etc.).

/**
 * Processed image output. [getOutputFileFromSource] value will be used by default if this is `null`.
 */
private var output: File? = null

/**
 * Gets the default output file. E.g.: hello_world.png -> hello_world_tag.png
 * @param source source image that is processed
 * @param tag suffix of the file name representing the operation executed.
 * @return default output file.
 */
private fun getOutputFileFromSource(source: File, tag: String) = File(source.parentFile, source.nameWithoutExtension + "_$tag." + source.extension)

/**
 * Saves the given [image] to a file.
 * @param image image to save
 * @param source source image, used for [getOutputFileFromSource] in case [output] is `null`
 * @param tag suffix of the file name, used for [getOutputFileFromSource] in case [output] is `null`
 * @return output file
 */
private fun save(image: BufferedImage, source: File, tag: String): File = (output ?: getOutputFileFromSource(source, tag)).also {
    PiktImage.saveImage(image, it)
}

/**
 * Triggered by -imgoutput=path argument.
 * Defines the output file name for the operations in this file. It must come before the operation command.
 *
 * @author Giorgio Garofalo
 */
class ImageOutputCommand : Command("-imgoutput", { args ->
    if(args == null) {
        System.err.println("Expected -imgoutput=<path>. Using default path.")
    } else if(output == null) {
        output = File(args)
    }
})

/**
 * Triggered by -standardize argument.
 *
 * @author Giorgio Garofalo
 */
class StandardizeCommand : Command("-standardize", {
    val properties = PiktPropertiesRetriever().retrieve()
    val sourceImage = PiktImage.readImage(properties.source)
    val finalImage = StandardizeImageProcessing(sourceImage, properties.colors.rawProperties).process()
    val file = save(finalImage, properties.source, tag = "standardized")

    println("Standardized image successfully saved as $file.")
}, closeOnComplete = true)

/**
 * Triggered by -recolorize[=method] argument.
 *
 * @author Giorgio Garofalo
 */
class RecolorizeCommand : Command("-recolorize", { args ->
    val properties = PiktPropertiesRetriever().retrieve()
    val sourceImage = PiktImage.readImage(properties.source)

    // Get color choice method from optional =method argument. Defaults to FIRST.
    val method = if(args != null && args.isNotEmpty()) {
        RecolorizeImageProcessing.ColorChoiceMethod.values().firstOrNull { it.name.equals(args, ignoreCase = true) }
                ?: RecolorizeImageProcessing.ColorChoiceMethod.FIRST.also {
                    System.err.println("Color choice method $args not found. Available methods: ${RecolorizeImageProcessing.ColorChoiceMethod.values().joinToString { it.name.lowercase() }}. Using 'first'.")
                }
    } else {
        RecolorizeImageProcessing.ColorChoiceMethod.FIRST
    }

    val finalImage = RecolorizeImageProcessing(sourceImage, properties.colors.rawProperties, method).process()
    val file = save(finalImage, properties.source, tag = "recolorized")

    println("Recolorized image successfully saved as $file.")
}, closeOnComplete = true)

/**
 * Triggered by -compact[=size] argument.
 *
 * @author Giorgio Garofalo
 */
class CompactCommand : Command("-compact", { args ->
    val properties = PiktPropertiesRetriever().retrieve()
    val piktImage = PiktImage(properties)

    val width: Int?
    val height: Int?

    if(args == null) {
        width = null
        height = null
    } else {
        // Extract from w?h? argument, where both w and h are optional.
        width = Regex("(?<=w)\\d+").find(args)?.value?.toInt()
        height = Regex("(?<=h)\\d+").find(args)?.value?.toInt()
    }

    val image = piktImage.compacter.compact(width, height)
    val file = save(image, properties.source, tag = "compacted")

    println("Compacted image successfully saved as $file.")
}, closeOnComplete = true)

/**
 * Triggered by -decompact argument.
 *
 * @author Giorgio Garofalo
 */
class DecompactCommand : Command("-decompact", {
    val properties = PiktPropertiesRetriever().retrieve()
    val piktImage = PiktImage(properties)

    val image = piktImage.compacter.decompact()
    val file = save(image, properties.source, tag = "decompacted")

    println("Decompacted image successfully saved as $file.")
}, closeOnComplete = true)

/**
 * Triggered by -standardecompact argument.
 *
 * -standardize + decompact
 *
 * @author Giorgio Garofalo
 */
class StandardizeDecompactCommand : Command("-standardecompact", {
    val properties = PiktPropertiesRetriever().retrieve()
    val piktImage = PiktImage(properties)

    val image = piktImage.compacter.decompact()
    val finalImage = StandardizeImageProcessing(image, properties.colors.rawProperties).process()

    val file = save(finalImage, properties.source, tag = "standardecompacted")

    println("Standardized and decompacted image successfully saved as $file.")
}, closeOnComplete = true)