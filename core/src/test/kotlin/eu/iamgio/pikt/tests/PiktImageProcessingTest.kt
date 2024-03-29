package eu.iamgio.pikt.tests

import eu.iamgio.pikt.command.commands.imageprocessing.*
import eu.iamgio.pikt.exit.ERROR_FAILED_IMAGE_PROCESSING
import eu.iamgio.pikt.exit.ExitAttemptException
import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

// These tests apply image processing filters to the same image (/imageprocessing-tests/source.png)
// which generates the following pseudocode:
// var str = "Pikt"
// if true
//     print str

private const val EXPECTED_RESULT = "Pikt" // Expected result from all the programs.

class PiktImageProcessingTest {

    private val launcher = PiktImageProcessingTestLauncher()

    // Launches a Pikt program.
    private fun launch(colorSchemeName: String? = null) = launcher.launch("", colorSchemeName)

    // Copies a resource from /imageprocessing-tests/ to the temp folder.
    private fun copy(name: String) = launcher.copy(name)

    // Gets the file path from file name within the temp folder.
    private fun pathify(name: String) = launcher.pathify(name)

    // Checks whether the transformed image outputs the correct result.
    private fun checkCorrectOutput(colorSchemeName: String? = null) =
            assertEquals(EXPECTED_RESULT, launch(colorSchemeName).firstOrNull())

    // Compares the output image to another image loaded from the internal tests folder
    private fun assertImageEquals(name: String) = launcher.assertImageEquals(name)

    // Compares the output image size to given values
    private fun assertImageSize(width: Int, height: Int) = with(outImage) {
        assertEquals(width, this.width)
        assertEquals(height, this.height)
    }

    // Latest generated image
    private val outImage: BufferedImage
        get() = launcher.getOutputImage()

    // Adapts the image to the test-scheme color scheme (-recolorize)
    @Test
    fun recolorize() {
        copy("test-scheme.properties")
        System.setProperty("colors", pathify("test-scheme"))

        RecolorizeCommand().execute()
        checkCorrectOutput(colorSchemeName = "test-scheme")
        launcher.assertImageEquals("exp_recolorized")

        System.clearProperty("colors")
    }

    // Adapts the recolorized image to the standard scheme (-standardize)
    @Test
    fun standardize() {
        copy("test-scheme.properties")
        copy("exp_recolorized.png")
        System.setProperty("source", pathify("exp_recolorized.png"))
        System.setProperty("colors", pathify("test-scheme"))

        StandardizeCommand().execute()
        launcher.assertImageEquals("source")

        System.clearProperty("colors")

        checkCorrectOutput()
        launcher.setDefaultSource()
    }

    // Compacts the image with automatic size (-compact)
    @Test
    fun compactAutosize() {
        CompactCommand().execute()
        checkCorrectOutput()

        assertImageSize(3, 4)
        assertImageEquals("exp_autocompact")
    }

    // Compacts the image with fixed size (-compact=<size>)
    @Test
    fun compactManualSize() {
        CompactCommand().execute("w5h3")
        checkCorrectOutput()

        assertImageSize(5, 3)
        assertImageEquals("exp_manualcompact")
    }

    // Tries to compact the image (10 elements) into a 9-elements output
    @Test
    fun compactManualSizeError() {
        val exception = assertFailsWith<ExitAttemptException> {
            CompactCommand().execute("w3h3")
        }
        println(exception.message)
        assertEquals(ERROR_FAILED_IMAGE_PROCESSING, exception.code)
    }

    // Expands the image (-decompact)
    @Test
    fun decompact() {
        DecompactCommand().execute()
        checkCorrectOutput()

        assertImageSize(6, 4)
        assertImageEquals("exp_decompact")
    }

    // Swaps a variable color with another (-colorswap=<swaps>)
    @Test
    fun colorswap() {
        ColorSwapCommand().execute("FCFF00:CDDAB6")
        checkCorrectOutput()
        assertImageEquals("exp_colorswap")
    }

    // Applies a mask image (-mask=<path>)
    @Test
    fun mask() {
        copy("face-mask.png")
        MaskCommand().execute(pathify("face-mask.png"))
        checkCorrectOutput()
        assertImageEquals("exp_mask")
    }
}