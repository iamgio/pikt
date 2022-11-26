package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.logger.Log
import eu.iamgio.pikt.properties.INTERNAL_COLORS_SCHEME_PATH
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.util.ERROR_BAD_IO
import eu.iamgio.pikt.util.ERROR_BAD_PROPERTIES
import eu.iamgio.pikt.util.exit
import java.io.FileOutputStream
import java.io.IOException

/**
 * Triggered by -createscheme argument
 *
 * @author Giorgio Garofalo
 */
class CreateSchemeCommand : Command("-createscheme", closeOnComplete = true) {
    override fun execute(args: String?) {
        val retriever = PiktPropertiesRetriever()
        val libraries = retriever.libraries()
        val file = retriever.colorsFile()
            ?: exit(ERROR_BAD_PROPERTIES, message = "Color scheme (-Dcolors) not set.")

        if(file.exists()) {
            Log.info("Overwriting color scheme.")
        } else {
            Log.info("Creating color scheme file.")
        }

        try {
            Command::class.java.getResourceAsStream(INTERNAL_COLORS_SCHEME_PATH)!!.copyTo(FileOutputStream(file))
            libraries.forEach { it.colorScheme?.appendToScheme(file) }
        } catch(e: IOException) {
            Log.error("An error occurred while creating color scheme:")
            e.printStackTrace()
            exit(ERROR_BAD_IO)
        }

        Log.info("Color scheme successfully created at $file")
    }
}