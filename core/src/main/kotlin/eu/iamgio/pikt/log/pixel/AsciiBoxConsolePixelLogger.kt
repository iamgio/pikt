package eu.iamgio.pikt.log.pixel

import eu.iamgio.pikt.image.Color

private const val TOP_LEFT_CORNER = '┌'
private const val TOP_RIGHT_CORNER = '┐'
private const val BOTTOM_LEFT_CORNER = '└'
private const val BOTTOM_RIGHT_CORNER = '┘'
private const val HORIZONTAL_LINE = '─'
private const val VERTICAL_LINE = '│'
private const val MARK = '✖'
private const val SPACER = ' '

/**
 * A logger that prints pixels as ASCII boxes with their hex code inside.
 *
 * @author Giorgio Garofalo
 */
class AsciiBoxConsolePixelLogger : ConsolePixelLogger() {

    private fun buildBox(color: Color, mark: Boolean, builder: AsciiBoxBuilder) {
        builder.text = " ${color.hexName} "
        builder.hasMark = mark
        builder.buildTop()
        builder.buildCenter()
        builder.buildBottom()
    }

    override fun logAll(colors: Iterable<Color>, markIndex: Int?) {
        val builder = AsciiBoxBuilder()

        colors.forEachIndexed { index, color ->
            buildBox(color, index == markIndex, builder)
        }

        super.newLine()
        val boxText = builder.boxText
        super.println(boxText)
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
     * Whether the box should contain a mark.
     */
    var hasMark: Boolean = false

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
            if(hasMark && text.isNotEmpty()) {
                // Add mark at the center of the box.
                append(HORIZONTAL_LINE.repeat(text.length / 2 - 1))
                append(SPACER)
                append(MARK)
                append(SPACER)
                append(HORIZONTAL_LINE.repeat(text.length / 2 - 1))
            } else {
                append(HORIZONTAL_LINE.repeat(text.length))
            }
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