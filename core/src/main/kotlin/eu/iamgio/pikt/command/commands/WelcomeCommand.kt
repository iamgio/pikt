package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.util.KotlinCompilerDownloader
import eu.iamgio.pikt.util.KotlinCompilerType
import java.io.FileOutputStream

/**
 * Sample hello world image.
 */
private const val HELLO_WORLD = "helloworld.png"

/**
 * Triggered by -welcome argument. Creates useful initial files for the first run.
 * It is also called by the GitHub action before zipping.
 *
 * @author Giorgio Garofalo
 */
class WelcomeCommand : Command("-welcome", closeOnComplete = true) {
    override fun execute(args: String?) {
        println("-----\nWelcome to Pikt!\n-----\n")

        // Target color scheme
        System.setProperty("colors", "colors")

        CreateSchemeCommand().execute()
        ExportSchemeCommand().execute()
        copyHelloWorld()
        KotlinCompilerDownloader.download(version = null, KotlinCompilerType.JVM)
    }

    private fun copyHelloWorld() {
        println("Copying $HELLO_WORLD")
        javaClass.getResourceAsStream("/$HELLO_WORLD")?.copyTo(FileOutputStream(HELLO_WORLD))
            ?: System.err.println("Could not find $HELLO_WORLD")
    }
}