package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.util.KotlinCompilerDownloader
import eu.iamgio.pikt.util.KotlinCompilerType

/**
 * Triggered by -welcome argument. Creates useful initial files for the first run.
 * It is also called by the GitHub action before zipping.
 *
 * @author Giorgio Garofalo
 */
class WelcomeCommand : Command("-welcome", {
    println("-----\nWelcome to Pikt!\n-----\n")

    CreateSchemeCommand().also { it.closeOnComplete = false }.execute("colors")
    ExportSchemeCommand().also { it.closeOnComplete = false }.execute("colors")
    KotlinCompilerDownloader.download(version = null, KotlinCompilerType.JVM)
}, closeOnComplete = true)