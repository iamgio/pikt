package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command

/**
 * Triggered by the `--chainoutput` argument.
 * Allows chaining between image transformation commands: the output of an image processing command becomes the input for the next command.
 * It is recommended to use in combination with [ImageOutputCommand].
 *
 * @see ImageProcessingUtils.enableChaining
 * @author Giorgio Garofalo
 */
class ChainOutputCommand : Command("--chainoutput", isSettingsCommand = true) {
    override fun execute(args: String?) {
        ImageProcessingUtils.enableChaining = true
    }
}