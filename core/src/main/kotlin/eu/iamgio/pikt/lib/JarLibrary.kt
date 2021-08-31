package eu.iamgio.pikt.lib

import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import java.io.File
import java.util.*

const val LIBRARY_COLOR_SCHEME_KEY_PREFIX = "lib."

/**
 * The color scheme bundled in external libraries.
 */
private const val LIBRARY_COLOR_SCHEME_NAME = "colors.properties"

/**
 * The library information specifications bundled in external libraries.
 */
const val LIBRARY_INFO_NAME = "lib-info.properties"

/**
 * A JAR library the Pikt program depends on to be included into the output executable.
 *
 * @param libraryJar JAR content as an archive
 * @author Giorgio Garofalo
 */
class JarLibrary(private val libraryJar: ZipFile) {

    /**
     * @param file JAR file on disk
     */
    constructor(file: File) : this(ZipFile(file))

    /**
     * JAR file.
     */
    val file: File
        get() = libraryJar.file

    /**
     * Absolute path of the JAR file.
     */
    val absolutePath: String
        get() = file.absolutePath

    /**
     * The name of this library.
     */
    val name: String
        get() = file.name

    /**
     * The (optional) color scheme bundled into the library.
     */
    val colorScheme: LibraryColorScheme?
        get() = libraryJar.getFileHeader(LIBRARY_COLOR_SCHEME_NAME)?.let { header ->
            LibraryColorScheme(this, libraryJar.getInputStream(header))
        }

    /**
     * Information of this library.
     */
    val info: LibraryInfo by lazy {
        val inputStream = libraryJar.getFileHeader(LIBRARY_INFO_NAME)?.let { header ->
            libraryJar.getInputStream(header)
        } ?: throw NullPointerException("Library $name does not include a $LIBRARY_INFO_NAME file.")

        LibraryInfo.fromProperties(Properties().also { it.load(inputStream) })
    }

    /**
     * Includes a library into the output JAR file.
     * @param targetJarFile target output JAR file
     */
    fun extractTo(targetJarFile: File) {
        val targetJar = ZipFile(targetJarFile)

        libraryJar.fileHeaders.filter {
            // Ignored files
            it.fileName != LIBRARY_COLOR_SCHEME_NAME && it.fileName != LIBRARY_INFO_NAME
                    && !it.fileName.startsWith("META-INF") && !it.fileName.endsWith("/")
        }.forEach { header ->
            val inputStream = libraryJar.getInputStream(header)
            targetJar.addStream(inputStream, ZipParameters().apply { fileNameInZip = header.fileName; isIncludeRootFolder = true })
            inputStream.close()
        }
    }
}