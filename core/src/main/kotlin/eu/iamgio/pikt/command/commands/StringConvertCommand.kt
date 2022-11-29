package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.image.Color
import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.log.pixel.ConsolePixelLogger
import eu.iamgio.pikt.logger.Log
import eu.iamgio.pikt.properties.ColorsPropertiesRetriever

/**
 * Triggered by -strconvert=string argument.
 * Converts a string into a sequence of RGB values supported by Pikt and prints them out.
 *
 * @author Giorgio Garofalo
 */
class StringConvertCommand : Command("-strconvert", closeOnComplete = true) {
    override fun execute(args: String?) {
        if(args == null) {
            // Read lines from stdin
            while(true) {
                print("> ")
                val text = readLine() ?: break
                if(text.isEmpty()) break
                convertAndPrint(text)
            }
        } else {
            convertAndPrint(args)
        }
    }

    /**
     * Converts a string into a sequence of character codes
     * and prints out both lines (ASCII string and numeric string).
     */
    private fun convertAndPrint(text: String) {
        val topLine = StringBuilder("RGB:  ")
        val bottomLine = StringBuilder(" ".repeat(topLine.length)) // The bottom line contains ASCII characters aligned with their code.
        val codes = mutableListOf<Int>() // Character codes

        var containsNullChar = false // Whether \0 (char code 0) appears within the string in order to show a warning message.

        text.forEach { char ->
            // Appends the char code and aligns the ASCII caracter below
            val code = "${char.code} "
            topLine.append(code)
            bottomLine.append(char).append(" ".repeat(code.length - 1))
            codes += char.code
            if(char.code == 0) containsNullChar = true
        }

        Log.info(topLine)
        Log.info(bottomLine)
        if(containsNullChar) Log.warn("The string contains a null character that could be misread.")

        logPixels(codes)
    }

    /**
     * Logs grayscale pixels with the active pixel logger.
     * @param codes list of R=G=B (grayscale) 0-255 values
     */
    private fun logPixels(codes: List<Int>) = Log.pixelLogger?.let { logger ->
        (logger as? ConsolePixelLogger)?.stream = System.out

        val colors = ColorsPropertiesRetriever(libraries = emptyList()).retrieve() // Dummy color data
        val pixels = codes.mapIndexed { index, rgb ->
            Pixel(Color.grayscale(rgb), x = index, y = 0, colors) // Grayscale pixels
        }

        logger.logAll(pixels)
    }
}