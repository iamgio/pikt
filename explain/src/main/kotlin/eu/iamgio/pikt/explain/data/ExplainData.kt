package eu.iamgio.pikt.explain.data

import eu.iamgio.pikt.explain.image.SourceImage
import eu.iamgio.pikt.explain.syntax.SyntaxHighlighting
import eu.iamgio.pikt.properties.Properties
import java.io.File

/**
 * Data needed for code explanation.
 *
 * @param image source Pikt image
 * @param output output image file
 * @param codeLines lines of explanation code
 * @param textComments text comments on the source image
 * @param lineComments line comments on the source image
 * @param imageSpecs style of the output image
 * @param syntaxHighlighting syntax highlighting rules
 * @author Giorgio Garofalo
 */
data class ExplainData(
    val image: SourceImage,
    val output: File,
    val codeLines: List<String>,
    val textComments: List<TextCommentData>,
    val lineComments: List<LineCommentData>,
    val imageSpecs: ImageSpecsData,
    val syntaxHighlighting: SyntaxHighlighting
) : Properties