package eu.iamgio.pikt.tests

import eu.iamgio.pikt.command.commands.imageprocessing.ColorSwapCommand
import eu.iamgio.pikt.command.commands.imageprocessing.CompactCommand
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

    // Compacts the image with automatic size (-compact)
    @Test
    fun compactAutosize() {
        CompactCommand().fire()
        checkCorrectOutput()

        with(outImage) {
            assertTrue {
                width == 3 && height == 4
            }
            assertTrue {
                getRGB(0, 3).rgbToHex() == "FCFF00"
                        && getRGB(1, 2).rgbToHex() == "32C832"
            }
        }
    }

    // Compacts the image with fixed size (-compact=<size>)
    @Test
    fun compactManualsize() {
        CompactCommand().fire("w5h3")
        checkCorrectOutput()

        with(outImage) {
            assertTrue {
                width == 5 && height == 3
            }
            assertTrue {
                getRGB(4, 1).rgbToHex() == "FCFF00"
                        && getRGB(2, 1).rgbToHex() == "32C832"
            }
        }
    }

    // Swaps a variable color with another (-colorswap=<swaps>)
    @Test
    fun colorswap() {
        ColorSwapCommand().fire("FCFF00:CDDAB6")
        checkCorrectOutput()

        with(outImage) {
            assertTrue {
                getRGB(1, 0).rgbToHex() == "CDDAB6"
                        && getRGB(1, 3).rgbToHex() == "CDDAB6"
            }
        }
    }

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