package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.PiktPropertiesRetriever

/**
 * Triggered by -decompact argument.
 *
 * @author Giorgio Garofalo
 */
class DecompactCommand : Command("-decompact", {
    val properties = PiktPropertiesRetriever().retrieve()
    val piktImage = PiktImage(properties)

    val image = piktImage.compacter.decompact()
    val file = ImageProcessingUtils.save(image, properties.source, tag = "decompacted")

    println("Decompacted image successfully saved as $file.")
}, closeOnComplete = true)