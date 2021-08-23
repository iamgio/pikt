package eu.iamgio.pikt.compiler

import java.io.File

/**
 * Executable starter scripts.
 *
 * @param type script type
 * @param content content of the file
 * @author Giorgio Garofalo
 */
class StarterScriptFile(
        private val type: Type,
        private val content: String
) {

    /**
     * Type of starter.
     * @param prefix script content prefix
     * @param suffix script content suffix
     * @param extension file extension
     */
    enum class Type(val prefix: String, val suffix: String, val extension: String) {
        /**
         * Batch script (Windows).
         */
        BAT(prefix = "", suffix = "\nPAUSE", extension = "bat"),

        /**
         * Shell script (Linux).
         */
        SH(prefix = "#!/bin/sh\n", suffix = "\nread -s -n 1 -p \"Press any key to continue . . .\"", extension = "sh"),


        /**
         * Command script (OSX).
         */
        COMMAND(prefix = SH.prefix, suffix = SH.suffix, extension = "command"),
    }

    /**
     * Creates the starter file.
     *
     * @param folder file folder
     * @param name file name without extension
     */
    fun create(folder: File, name: String) {
        File(folder, "$name.${type.extension}").writeText(type.prefix + content + type.suffix)
    }
}