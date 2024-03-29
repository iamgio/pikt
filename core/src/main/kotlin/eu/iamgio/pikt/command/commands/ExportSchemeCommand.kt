package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.exit.ERROR_BAD_IO
import eu.iamgio.pikt.exit.ERROR_BAD_PROPERTIES
import eu.iamgio.pikt.exit.exit
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.schemes.ColorSchemePalette
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Triggered by the `exportscheme` argument.
 * Creates a color palette.
 *
 * @author Giorgio Garofalo
 */
class ExportSchemeCommand : Command("exportscheme", closeOnComplete = true) {
    override fun execute(args: String?) {
        val schemeFile = PiktPropertiesRetriever().colorsFile()
            ?: exit(ERROR_BAD_PROPERTIES, message = "Color scheme (-Dcolors) not set.")

        val imageFile = File("${schemeFile.nameWithoutExtension}.png")

        if(imageFile.exists()) {
            Log.info("Overwriting color palette.")
        } else {
            Log.info("Creating color palette.")
        }

        try {
            ColorSchemePalette(FileInputStream(schemeFile)).generate(imageFile)
        } catch(e: IOException) {
            Log.error("An error occurred while exporting color scheme:")
            e.printStackTrace()
            exit(ERROR_BAD_IO)
        }

        Log.info("Color palette successfully generated at $schemeFile")
    }
}