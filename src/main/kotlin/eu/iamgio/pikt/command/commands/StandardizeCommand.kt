package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.schemes.StandardizeImageProcessing
import java.io.File
import kotlin.system.exitProcess

/**
 * Triggered by -standardize argument.
 * @author Giorgio Garofalo
 */
class StandardizeCommand : Command("-standardize", {
    val properties = PiktPropertiesRetriever().retrieve()
    val sourceImage = PiktImage.readImage(properties.source)
    val finalImage = StandardizeImageProcessing(sourceImage, properties.colors.rawProperties).process()
    val file = File(properties.source.parentFile, "standardized_" + properties.source.name)
    PiktImage.saveImage(finalImage, file)

    println("Standardized image successfully saved as $file.")
    exitProcess(0)
})