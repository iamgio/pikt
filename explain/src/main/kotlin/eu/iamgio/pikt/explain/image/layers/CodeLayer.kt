package eu.iamgio.pikt.explain.image.layers

import eu.iamgio.pikt.explain.data.ImageSpecsData
import eu.iamgio.pikt.explain.image.calcImageCenteredTextY
import eu.iamgio.pikt.explain.syntax.SyntaxHighlighting
import eu.iamgio.pikt.explain.syntax.SyntaxHighlightingEntryStyle
import eu.iamgio.pikt.explain.syntax.SyntaxHighlightingMatch
import java.awt.Font
import java.awt.Graphics2D

/**
 * Character to use to calculate a font's character width.
 * Given a monospaced font, the chosen character is not relevant.
 */
private const val CHAR_WIDTH_DUMMY_VALUE = ' '

/**
 * Numbers of space characters to build a tabulation.
 */
private const val TAB_SPACES_AMOUNT = 4

/**
 * Layer with text of code.
 *
 * @param codeLines lines of code to display
 * @param font font used
 * @param x X coordinate of the text
 * @author Giorgio Garofalo
 */
class CodeLayer(
    private val codeLines: List<String>,
    private val syntaxHighlighting: SyntaxHighlighting,
    private val font: Font,
    private val x: Int
) : ImageLayer {

    override fun draw(graphics: Graphics2D, imageSpecs: ImageSpecsData, imageWidth: Int, imageHeight: Int) {
        graphics.font = this.font

        this.codeLines.forEachIndexed { index, line ->
            val groups = this.syntaxHighlighting.getGroups(this.replaceTabs(line))
            val y = imageSpecs.calcImageCenteredTextY(index)

            this.drawGroups(groups, imageSpecs, y, graphics)
        }
    }

    /**
     * Substitutes tabulations with an amount of spaced defined by [TAB_SPACES_AMOUNT].
     * @param text text to substitute tabs from
     * @return a copy of [text] with spaces instead of tabs
     */
    private fun replaceTabs(text: String) = text.replace("\t", " ".repeat(TAB_SPACES_AMOUNT))

    /**
     * Calculates the width used by a character with the given [font].
     * Note that [CHAR_WIDTH_DUMMY_VALUE] is used, hence the result with
     * non-monospaced fonts is unpredictable.
     */
    private fun Graphics2D.getCharacterWidth(): Int {
        return this.fontMetrics.charWidth(CHAR_WIDTH_DUMMY_VALUE)
    }

    /**
     * Draws the content of match groups with their styles *and* the remaining text.
     * @param groups syntax highlighting match groups
     * @param imageSpecs image styles
     * @param y Y coordinate of the text
     * @param graphics graphics to draw on
     */
    private fun drawGroups(
        groups: List<SyntaxHighlightingMatch>,
        imageSpecs: ImageSpecsData,
        y: Int,
        graphics: Graphics2D
    ) {
        val charWidth = graphics.getCharacterWidth()

        groups.forEach { match ->
            val x = this.x + match.range.first * charWidth
            this.applyMatchStyle(match, imageSpecs, graphics)

            graphics.drawString(
                match.content,
                x, y
            )
        }
    }

    /**
     * Applies the properties of a [SyntaxHighlightingEntryStyle] to the image [graphics].
     * @param match match to extract the values from
     * @param imageSpecs image styles to get the default values from,
     *  in case of a group not matching any syntax highlighting rule.
     * @param graphics graphics to set values to
     */
    private fun applyMatchStyle(match: SyntaxHighlightingMatch, imageSpecs: ImageSpecsData, graphics: Graphics2D) {
        graphics.color = match.entry?.style?.color ?: imageSpecs.textColor
    }
}