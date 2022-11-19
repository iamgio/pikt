package eu.iamgio.pikt.command.commands

import eu.iamgio.pikt.command.Command

/**
 * Triggered by -strconvert=string argument.
 * Converts a string into a sequence of RGB values supported by Pikt and prints them out.
 *
 * @author Giorgio Garofalo
 */
class StringConvertCommand : Command("-strconvert", closeOnComplete = true) {
    override fun execute(args: String?) {
        if(args == null) {
            System.err.println("Usage: -strconvert=<string>. Exiting.")
            return
        }

        val prefix = "RGB:  "
        var bottomLine = " ".repeat(prefix.length) // The bottom line contains ASCII characters aligned with their code.

        var containsNullChar = false // Whether \0 (char code 0) appears within the string in order to show a warning message.

        print(prefix)

        args.toCharArray().forEach { char ->
            // Prints the char code and aligns the ASCII caracter below
            print("${char.code}  ".also { bottomLine += char + " ".repeat(it.length - 1) })
            if(char.code == 0) containsNullChar = true
        }

        println()
        println(bottomLine)
        if(containsNullChar) println("(!) The string contains a null character that could be misread by Pikt.")
    }
}