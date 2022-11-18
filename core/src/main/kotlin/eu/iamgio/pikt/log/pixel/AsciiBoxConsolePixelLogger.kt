package eu.iamgio.pikt.log.pixel

import eu.iamgio.pikt.image.Pixel
import java.io.PrintStream

private const val TOP_LEFT_CORNER = '┌'
private const val TOP_RIGHT_CORNER = '┐'
private const val BOTTOM_LEFT_CORNER = '└'
private const val BOTTOM_RIGHT_CORNER = '┘'
private const val HORIZONTAL_LINE = '─'
private const val VERTICAL_LINE = '│'
private const val SPACER = ' '

/**
 * A logger that prints pixels as ASCII boxes with their hex code inside.
 *
 * @param stream target stream to print on
 * @author Giorgio Garofalo
 */
class AsciiBoxConsolePixelLogger(stream: PrintStream) : ConsolePixelLogger(stream) {

    private val builder = AsciiBoxBuilder()

    override fun log(pixel: Pixel, mark: Boolean) {
        builder.text = " ${pixel.hexName} "
        builder.buildTop()
        builder.buildCenter()
        builder.buildBottom()
    }

    override fun newLine() {
        super.newLine()
        val boxText = builder.boxText
        stream.println(boxText)
        builder.clear()
    }
}

/**
 * Builds the ASCII box.
 */
private class AsciiBoxBuilder {
    private val builder1 = StringBuilder()
    private val builder2 = StringBuilder()
    private val builder3 = StringBuilder()

    /**
     * The input text.
     */
    var text: String = ""

    /**
     * The output text.
     */
    val boxText: String
        get() = buildString {
            append(builder1)
            append('\n')
            append(builder2)
            append('\n')
            append(builder3)
        }

    /**
     * Builds the top part of the box.
     */
    fun buildTop() {
        with(builder1) {
            append(TOP_LEFT_CORNER)
            append(HORIZONTAL_LINE.repeat(text.length))
            append(TOP_RIGHT_CORNER)
            append(SPACER)
        }
    }

    /**
     * Builds the central part of the box.
     */
    fun buildCenter() {
        with(builder2) {
            append(VERTICAL_LINE)
            append(text)
            append(VERTICAL_LINE)
            append(SPACER)
        }
    }

    /**
     * Builds the bottom part of the box.
     */
    fun buildBottom() {
        with(builder3) {
            append(BOTTOM_LEFT_CORNER)
            append(HORIZONTAL_LINE.repeat(text.length))
            append(BOTTOM_RIGHT_CORNER)
            append(SPACER)
        }
    }

    /**
     * Resets the status to an empty string.
     */
    fun clear() {
        builder1.clear()
        builder2.clear()
        builder3.clear()
    }

    private fun Char.repeat(n: Int) = toString().repeat(n)
}