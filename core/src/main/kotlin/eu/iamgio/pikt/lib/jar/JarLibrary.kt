package eu.iamgio.pikt.lib.jar

import eu.iamgio.pikt.lib.Library
import eu.iamgio.pikt.lib.LibraryColorScheme
import eu.iamgio.pikt.lib.LibraryFunction
import eu.iamgio.pikt.lib.LibraryInfo
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import java.io.File
import java.util.*

/**
 * The color scheme bundled in external libraries.
 */
private const val LIBRARY_COLOR_SCHEME_NAME = "colors.properties"

/**
 * The library information specifications bundled in external libraries.
 */
private const val LIBRARY_INFO_NAME = "lib-info.properties"

/**
 * A JAR library the Pikt program depends on to be included into the output executable.
 *
 * @param libraryJar JAR content as an archive
 * @author Giorgio Garofalo
 */
class JarLibrary(private val libraryJar: ZipFile) : Library {

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

    override val name: String
        get() = file.name

    override val info: LibraryInfo by lazy {
        val inputStream = libraryJar.getFileHeader(LIBRARY_INFO_NAME)?.let { header ->
            libraryJar.getInputStream(header)
        } ?: throw NullPointerException("Library $name does not include a $LIBRARY_INFO_NAME file.")

        LibraryInfo.fromProperties(Properties().also { it.load(inputStream) })
    }

    override val colorScheme: LibraryColorScheme?
        get() = libraryJar.getFileHeader(LIBRARY_COLOR_SCHEME_NAME)?.let { header ->
            LibraryColorScheme(this, libraryJar.getInputStream(header))
        }

    /**
     * A reflection helper for this library
     */
    private val reflectionHelper = JarLibraryReflectionHelper(this)

    /**
     * Scans the JAR for .class files.
     * @return a sequence of reflection-ready classes within this JAR
     */
    fun getClasses(): Sequence<String> =
            libraryJar.fileHeaders.asSequence()
                    .filter { it.fileName.endsWith(".class") }
                    .map { it.fileName }
                    .map { it.replace("/", ".") }
                    .map { it.removeSuffix(".class") }

    override fun getFunctions(): List<LibraryFunction> {
        return this.reflectionHelper.getAllMethods().map { JarLibraryFunction(it) }
    }

    /**
     * Includes a library into the output JAR file.
     * @param executableFile target output JAR file
     */
    override fun applyTo(executableFile: File) {
        val targetJar = ZipFile(executableFile)

        libraryJar.fileHeaders.asSequence()
                // Ignoring non-relevant content.
                .filter { it.fileName != LIBRARY_COLOR_SCHEME_NAME }
                .filter { it.fileName != LIBRARY_INFO_NAME }
                .filterNot { it.fileName.startsWith("META-INF") }
                .filterNot { it.fileName.endsWith("/") }
                // Copying the resource to the target file.
                .forEach { header ->
                    libraryJar.getInputStream(header).use {
                        val parameters = ZipParameters().apply {
                            fileNameInZip = header.fileName
                            isIncludeRootFolder = true
                        }
                        targetJar.addStream(it, parameters)
                    }
                }
    }
}