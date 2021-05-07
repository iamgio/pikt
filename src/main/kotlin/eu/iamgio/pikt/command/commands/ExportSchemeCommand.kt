package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.schemes.ColorSchemePalette
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import kotlin.system.exitProcess

/**
 * Triggered by -exportscheme=name argument.
 * Creates a color palette.
 *
 * @author Giorgio Garofalo
 */
class ExportSchemeCommand : Command("-exportscheme", { args ->
    if(args == null) {
        System.err.println("Color scheme path not set. Usage: -exportscheme=path.\nExiting.")
        exitProcess(-1)
    }

    val schemeFile = File("$args.properties")
    val imageFile = File("$args.png")

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

    exitProcess(0)
})