package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.PiktPropertiesRetriever

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
    val file = ImageProcessingUtils.save(image, properties.source, tag = "compacted")

    println("Compacted image successfully saved as $file.")
}, closeOnComplete = true)