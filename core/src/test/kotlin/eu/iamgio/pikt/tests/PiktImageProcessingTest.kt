package eu.iamgio.pikt.tests

import eu.iamgio.pikt.command.commands.imageprocessing.MaskCommand
import eu.iamgio.pikt.image.rgbToHex
import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage
import kotlin.test.assertTrue

// These tests apply image processing filters to the same image (/imageprocessing-tests/source.png)
// which generates the following pseudocode:
// var str = "Pikt"
// if true
//     print str

private const val EXPECTED_RESULT = "Pikt" // Expected result from all the programs.

class PiktImageProcessingTest {

    private val launcher = PiktImageProcessingTestLauncher()

    // Launches a Pikt program.
    private fun launch() = launcher.launch()

    // Copies a resource from /imageprocessing-tests/ to the temp folder.
    private fun copy(name: String) = launcher.copy(name)

    // Gets the file path from file name within the temp folder.
    private fun pathify(name: String) = launcher.pathify(name)

    // Checks whether the transformed image outputs the correct result.
    private fun checkCorrectOutput() = assertTrue { launch().firstOrNull() == EXPECTED_RESULT }

    // Latest generated image
    private val outImage: BufferedImage
        get() = launcher.getOutputImage()

    // Applies a mask image (-mask=<path>)
    @Test
    fun mask() {
        copy("face-mask.png")
        MaskCommand().fire(pathify("face-mask.png"))
        checkCorrectOutput()

        with(outImage) {
            assertTrue {
                getRGB(0, 0).rgbToHex() == "FFFFFF" // Top left corner, not in mask
                        && getRGB(3, 4).rgbToHex() == "FF0000" // Left eye
                        && getRGB(9, 4).rgbToHex() == "505050" // Right eye
                        && getRGB(6, 7).rgbToHex() == "FF65FF" // Mouth
            }
        }
    }
}