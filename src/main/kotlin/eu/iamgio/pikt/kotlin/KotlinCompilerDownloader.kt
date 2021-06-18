package eu.iamgio.pikt.kotlin

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
private const val DEFAULT_KOTLIN_VERSION = "1.5.10"

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
        val url = MessageFormat.format(DOWNLOAD_URL, version ?: DEFAULT_KOTLIN_VERSION, type.downloadName, type.extension)

        println("Downloading $url. Please wait...")

        // Download using NIO
        val channel = Channels.newChannel(URL(url).openStream())
        val fos = FileOutputStream(File(folder, url.substring(url.lastIndexOf("/"))))
        fos.channel.transferFrom(channel, 0, Long.MAX_VALUE).also {
            println("Done. $it bytes downloaded.")
        }
    }
}

/**
 * Kotlin compiler types.
 */
enum class KotlinCompilerType(val downloadName: String, val extension: String) {

    JVM("compiler", "zip"),
    WINDOWS("native-windows", "zip"),
    MACOS("native-macos", "tar.gz"),
    LINUX("native-linux", "tar.gz")
}