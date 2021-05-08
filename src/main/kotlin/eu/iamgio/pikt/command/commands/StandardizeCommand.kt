package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.schemes.RecolorizeImageProcessing
import eu.iamgio.pikt.schemes.StandardizeImageProcessing
import java.io.File
import kotlin.system.exitProcess

/**
 * Triggered by -standardize argument.
 *
 * @author Giorgio Garofalo
 */
class StandardizeCommand : Command("-standardize", {
    val properties = PiktPropertiesRetriever().retrieve()
    val sourceImage = PiktImage.readImage(properties.source)
    val finalImage = StandardizeImageProcessing(sourceImage, properties.colors.rawProperties).process()
    val file = File(properties.source.parentFile, properties.source.name + "_standardized")
    PiktImage.saveImage(finalImage, file)

    println("Standardized image successfully saved as $file.")
    exitProcess(0)
})

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
    val file = File(properties.source.parentFile, properties.source.name + "_recolorized")
    PiktImage.saveImage(finalImage, file)

    println("Recolorized image successfully saved as $file.")
    exitProcess(0)
})