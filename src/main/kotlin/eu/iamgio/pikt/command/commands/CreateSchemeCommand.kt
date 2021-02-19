package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.system.exitProcess

/**
 * Triggered by -createscheme=name argument
 *
 * @author Giorgio Garofalo
 */
class CreateSchemeCommand : Command("-createscheme", { args ->
    if(args == null) {
        System.err.println("Color scheme path not set. Usage: -createscheme=path.\nExiting.")
        exitProcess(-1)
    }

    val file = File("$args.properties")

    if(file.exists()) {
        println("Overwriting color scheme.")
    } else {
        println("Creating color scheme file.")
    }

    try {
        Command::class.java.getResourceAsStream("/properties/colors.properties").copyTo(FileOutputStream(file))
    } catch(e: IOException) {
        System.err.println("An error occurred while creating color scheme:")
        e.printStackTrace()
        exitProcess(-1)
    }

    println("Color scheme successfully created at $file")

    exitProcess(0)
})