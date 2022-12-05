package eu.iamgio.pikt.command.commands.imageprocessing

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.log.Log
import java.io.File

/**
 * Triggered by -imgoutput=path argument.
 * Defines the output file name for the operations in this file.
 *
 * @see ImageProcessingUtils.output
 * @author Giorgio Garofalo
 */
class ImageOutputCommand : Command("-imgoutput", isSettingsCommand = true) {
    override fun execute(args: String?) {
        if(args == null) {
            Log.warn("Expected -imgoutput=<path>. Using default path.")
            return
        }
        if(ImageProcessingUtils.output == null) {
            ImageProcessingUtils.output = File(args)
        }
    }
}