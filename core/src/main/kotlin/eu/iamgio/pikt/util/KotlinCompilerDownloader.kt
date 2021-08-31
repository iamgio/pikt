package eu.iamgio.pikt.util

import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.text.MessageFormat


/**
 * Download URL of the compiler.
 *
 * {0} = version,
 * {1} = compiler name,
 * {2} = extension.
 */
private const val DOWNLOAD_URL = "https://github.com/JetBrains/kotlin/releases/download/v{0}/kotlin-{1}-{0}.{2}"

/**
 * Default Kotlin version to download if not specified.
 */
private const val DEFAULT_KOTLIN_VERSION = "1.5.30"

/**
 * Downloads the Kotlin compiler from GitHub releases.
 *
 * @author Giorgio Garofalo
 */
object KotlinCompilerDownloader {

    /**
     * Downloads the Kotlin compiler.
     * @param version Kotlin version. [DEFAULT_KOTLIN_VERSION] if `null`
     * @param type compiler type
     * @param folder target directory
     */
    fun download(version: String?, type: KotlinCompilerType, folder: File = File(".")) {
        val url = MessageFormat.format(DOWNLOAD_URL, version
                ?: DEFAULT_KOTLIN_VERSION, type.downloadName, type.extension)

        print("Downloading $url")

        val connection = URL(url).openConnection()
        println(" [${connection.contentLength * 10 / 1024 / 1024 / 10.0} MB]") // File size, one decimal.

        // 'Please wait...' animated output.
        val waitingText = AnimatedWaitMessage()
        waitingText.print()

        // Download using NIO
        val channel = Channels.newChannel(connection.getInputStream())
        val file = File(folder, url.substring(url.lastIndexOf("/")))
        FileOutputStream(file).channel.transferFrom(channel, 0, Long.MAX_VALUE)

        waitingText.stop(message = "Done.\nSaved to ${file.canonicalPath}")
    }
}

/**
 * Kotlin compiler types.
 *
 * URL: https://github.com/JetBrains/kotlin/releases/download/vVERSION/kotlin-[downloadName]-VERSION.[extension]
 */
enum class KotlinCompilerType(val downloadName: String, val extension: String) {

    JVM("compiler", "zip"),
    WINDOWS("native-windows", "zip"),
    MACOS("native-macos", "tar.gz"),
    LINUX("native-linux", "tar.gz")
}