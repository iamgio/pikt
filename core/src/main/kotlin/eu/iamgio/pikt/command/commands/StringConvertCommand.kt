package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command
import eu.iamgio.pikt.logger.Log

/**
 * Triggered by -strconvert=string argument.
 * Converts a string into a sequence of RGB values supported by Pikt and prints them out.
 *
 * @author Giorgio Garofalo
 */
class StringConvertCommand : Command("-strconvert", closeOnComplete = true) {
    override fun execute(args: String?) {
        if(args == null) {
            Log.error("Usage: -strconvert=<string>. Exiting.")
            return
        }

        var topLine = "RGB:  "
        var bottomLine = " ".repeat(topLine.length) // The bottom line contains ASCII characters aligned with their code.

        var containsNullChar = false // Whether \0 (char code 0) appears within the string in order to show a warning message.

        args.toCharArray().forEach { char ->
            // Appens the char code and aligns the ASCII caracter below
            val append = "${char.code} "
            topLine += append
            bottomLine += char + " ".repeat(append.length - 1)
            if(char.code == 0) containsNullChar = true
        }

        Log.info(topLine)
        Log.info(bottomLine)
        if(containsNullChar) Log.warn("(!) The string contains a null character that could be misread.")
    }
}