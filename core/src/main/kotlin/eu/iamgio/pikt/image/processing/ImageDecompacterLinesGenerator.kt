package eu.iamgio.pikt.image.processing

import eu.iamgio.pikt.image.PixelReader
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.statements.LambdaCloseStatement
import eu.iamgio.pikt.statement.statements.LambdaOpenStatement

/**
 * Generates lines for an [ImageDecompacter].
 *
 * @author Giorgio Garofalo
 */
class ImageDecompacterLinesGenerator {

    /**
     * Lines of the decompacted image.
     * Each line corresponds to a statement and is composed by a sequence of RGB values, one for each pixel.
     * A `null` value is considered as an empty pixel.
     */
    private val lines: MutableList<List<Int?>> = mutableListOf()

    /**
     * The current line spacing strategy.
     */
    private var currentDecompactionStyle = Statement.DecompactionStyle.NO_SPACING

    /**
     * TAB-like spaces to distinguish sub-blocks (defined by lambda blocks).
     */
    private var currentShift: Int = 0

    /**
     * Adds [line] to the final lines, if it is suitable.
     * @param line sequence of RGB values
     */
    private fun addLine(line: List<Int?>) {
        if(line.isNotEmpty() || this.lines.lastOrNull()?.isNotEmpty() == true) {
            this.lines += line
        }
    }

    /**
     * Adds [line] to the final lines and applies the line spacing strategy.
     * @param line sequence of RGB values
     * @param isLast whether this is the last line of the image
     */
    private fun append(line: List<Int?>, isLast: Boolean = false) {
        // Add empty line before the statement if required.
        if(currentDecompactionStyle.hasEmptyLineBefore) {
            addLine(emptyList())
        }

        // Add current line.
        addLine(line)

        // Add empty line after the statement if required.
        if(currentDecompactionStyle.hasEmptyLineAfter && !isLast) {
            addLine(emptyList())
        }
    }

    /**
     * Changes the shift (amount of TABs) if there is a lambda statement and adds `null` RGB values for each shift.
     * @param line sequence of RGB values
     * @param statement statement of this line
     */
    private fun applyShift(line: MutableList<Int?>, statement: Statement) {
        if(statement is LambdaCloseStatement) {
            currentShift--
        }

        repeat(currentShift) {
            line += null
        }

        if(statement is LambdaOpenStatement) {
            currentShift++
        }
    }

    /**
     * Generates decompacted lines from flat pixels.
     * @param reader pixel source
     * @return a line for each statement, plus possible empty lines depending on spacing strategies
     */
    fun generate(reader: PixelReader): List<List<Int?>> {
        var currentLine = mutableListOf<Int?>()

        reader.whileNotNull { pixel ->
            if(pixel.isStatement) {
                if(currentLine.isNotEmpty()) {
                    this.append(currentLine)
                }
                currentLine = mutableListOf()
                this.applyShift(currentLine, pixel.statement!!)

                currentDecompactionStyle = pixel.statement!!.decompactionStyle
            }
            currentLine += pixel.color.rgb
        }
        if(currentLine.isNotEmpty()) {
            this.append(currentLine, isLast = true)
        }

        return this.lines
    }
}