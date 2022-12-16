package eu.iamgio.pikt.tests

import eu.iamgio.pikt.command.commands.imageprocessing.ImageOutputCommand
import eu.iamgio.pikt.image.readLineByLine
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.util.NO_EXIT_PROPERTY
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import javax.imageio.ImageIO
import kotlin.test.assertTrue

private const val TESTS_FOLDER = "/imageprocessing-tests"
private const val SOURCE = "source.png"
private const val OUT = "out.png"

class PiktImageProcessingTestLauncher : PiktTestLauncher() {
    private val folder: File = File(tempDirectory, "imageprocessing")

    private val sourceFilePath: String
        get() = folder.absolutePath + File.separator + SOURCE

    private val outFilePath: String
        get() = folder.absolutePath + File.separator + OUT

    init {
        folder.mkdirs()
        Log.info("Storing to: $folder")

        setNoExit()
        setDefaultSource()
        // Set output file
        ImageOutputCommand().execute(outFilePath)
        copy("source.png")
    }

    fun setDefaultSource() {
        System.setProperty("source", sourceFilePath)
    }

    private fun setNoExit() {
        System.setProperty(NO_EXIT_PROPERTY, "")
    }

    fun copy(name: String) {
        Files.copy(
                PiktImageProcessingTest::class.java.getResourceAsStream("$TESTS_FOLDER/$name")!!,
                File(folder, name).toPath(),
                StandardCopyOption.REPLACE_EXISTING
        )
    }

    fun getOutputImage(): BufferedImage = ImageIO.read(getImage(""))

    fun pathify(name: String) = folder.absolutePath + File.separator + name

    fun assertImageEquals(name: String) {
        val image1 = getOutputImage()
        val image2 = ImageIO.read(PiktTest::class.java.getResourceAsStream("$TESTS_FOLDER/$name.png")!!)
        assertTrue(message = "Different size") {
            image1.width == image2.width
                    && image1.height == image2.height
        }
        assertTrue {
            var equals = true
            image1.readLineByLine { x, y ->
                if(image1.getRGB(x, y) != image2.getRGB(x, y)) {
                    Log.error("Different value at [$x, $y]")
                    equals = false
                }
            }
            equals
        }
    }

    override fun getImage(name: String): InputStream {
        return FileInputStream(outFilePath)
    }

    override fun getColorScheme(colorSchemeName: String): InputStream {
        return PiktTest::class.java.getResourceAsStream("$TESTS_FOLDER/$colorSchemeName.properties")!!
    }
}