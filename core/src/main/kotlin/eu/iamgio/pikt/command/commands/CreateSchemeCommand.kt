package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.properties.INTERNAL_COLORS_SCHEME_PATH
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import java.io.FileOutputStream
import java.io.IOException
import kotlin.system.exitProcess

/**
 * Triggered by -createscheme argument
 *
 * @author Giorgio Garofalo
 */
class CreateSchemeCommand : Command("-createscheme", {
    val retriever = PiktPropertiesRetriever()
    val file = retriever.colorsFile()
    val libraries = retriever.libraries()

    if(file == null) {
        System.err.println("Color scheme (-Dcolors) not set.\nExiting.")
        exitProcess(-1)
    }

    if(file.exists()) {
        println("Overwriting color scheme.")
    } else {
        println("Creating color scheme file.")
    }

    try {
        Command::class.java.getResourceAsStream(INTERNAL_COLORS_SCHEME_PATH)!!.copyTo(FileOutputStream(file))
        libraries.forEach { it.colorScheme?.appendToScheme(file) }
    } catch(e: IOException) {
        System.err.println("An error occurred while creating color scheme:")
        e.printStackTrace()
        exitProcess(-1)
    }

    println("Color scheme successfully created at $file")
}, closeOnComplete = true)