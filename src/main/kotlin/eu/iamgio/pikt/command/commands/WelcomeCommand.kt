package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.kotlin.KotlinCompilerDownloader
import eu.iamgio.pikt.kotlin.KotlinCompilerType
import java.io.File
import kotlin.system.exitProcess

/**
 * Triggered by -welcome argument. Creates useless initial files for the first run.
 * It is also called by the GitHub action before zipping.
 *
 * @author Giorgio Garofalo
 */
class WelcomeCommand : Command("-welcome", {
    KotlinCompilerDownloader.download(null, KotlinCompilerType.JVM, File("."))
    exitProcess(0)
})