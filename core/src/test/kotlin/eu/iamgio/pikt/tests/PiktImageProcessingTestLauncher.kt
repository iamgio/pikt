package eu.iamgio.pikt.tests

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.command.commands.imageprocessing.ImageOutputCommand
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import javax.imageio.ImageIO

private const val TESTS_FOLDER = "/imageprocessing-tests"
private const val SOURCE = "source.png"
private const val OUT = "out.png"

fun Command.fire(args: String? = null) = this.action(args)

class PiktImageProcessingTestLauncher : PiktTestLauncher() {
    private val folder: File = File(tempDirectory, "imageprocessing")
    private val sourceFilePath: String
        get() = folder.absolutePath + File.separator + OUT

    init {
        folder.mkdirs()
        println("Storing to: $folder")

        System.setProperty("source", sourceFilePath)
        // Set output file
        ImageOutputCommand().fire(sourceFilePath)
        copy("source.png")
    }

    fun copy(name: String) {
        Files.copy(
                PiktImageProcessingTest::class.java.getResourceAsStream("$TESTS_FOLDER/$name")!!,
                File(folder, name).toPath(),
                StandardCopyOption.REPLACE_EXISTING
        )
    }

    fun getOutputImage(): BufferedImage = ImageIO.read(getImage(""))

    fun launch() = launch("")

    fun pathify(name: String) = folder.absolutePath + File.separator + name

    override fun getImage(name: String): InputStream {
        return FileInputStream(sourceFilePath)
    }

    override fun getColorScheme(colorSchemeName: String): InputStream {
        return PiktTest::class.java.getResourceAsStream("/schemes/$colorSchemeName.properties")!!
    }
}