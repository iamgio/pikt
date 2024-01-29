package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.processing.ImageRecolorizer
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktPropertiesRetriever

/**
 * Triggered by the `recolorize[=method]` argument.
 *
 * @author Giorgio Garofalo
 */
class RecolorizeCommand : Command("recolorize", closeOnComplete = true) {
    override fun execute(args: String?) {
        val properties = PiktPropertiesRetriever().retrieve()
        val sourceImage = ImageProcessingUtils.read(properties.source)

        // Get color choice method from optional =method argument. Defaults to FIRST.
        val method = if(!args.isNullOrEmpty()) {
            ImageRecolorizer.ColorChoiceMethod.values().firstOrNull { it.name.equals(args, ignoreCase = true) }
                ?: ImageRecolorizer.ColorChoiceMethod.FIRST.also {
                    Log.warn(
                        "Color choice method $args not found. Available methods: ${
                            ImageRecolorizer.ColorChoiceMethod.values().joinToString { it.name.lowercase() }
                        }. Using 'first'."
                    )
                }
        } else {
            ImageRecolorizer.ColorChoiceMethod.FIRST
        }

        val finalImage = ImageRecolorizer(
            sourceImage,
            properties.colors.rawProperties,
            properties.libraries,
            method
        ).process()

        val file = ImageProcessingUtils.save(finalImage, properties.source, tag = "recolorized")

        Log.info("Recolorized image successfully saved as $file.")
    }
}