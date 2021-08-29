package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.schemes.ColorSchemePalette
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import kotlin.system.exitProcess

/**
 * Triggered by -exportscheme argument.
 * Creates a color palette.
 *
 * @author Giorgio Garofalo
 */
class ExportSchemeCommand : Command("-exportscheme", {
    val schemeFile = PiktPropertiesRetriever().colorsFile()

    if(schemeFile == null) {
        System.err.println("Color scheme (-Dcolors) not set.\nExiting.")
        exitProcess(-1)
    }

    val imageFile = File("${schemeFile.nameWithoutExtension}.png")

    if(imageFile.exists()) {
        println("Overwriting color palette.")
    } else {
        println("Creating color palette.")
    }

    try {
        ColorSchemePalette(FileInputStream(schemeFile)).generate(imageFile)
    } catch(e: IOException) {
        System.err.println("An error occurred while exporting color scheme:")
        e.printStackTrace()
        exitProcess(-1)
    }

    println("Color palette successfully generated at $schemeFile")
}, closeOnComplete = true)