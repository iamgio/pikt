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
     * @param extension file extension
     */
    enum class Type(val prefix: String, val suffix: String, val extension: String) {
        /**
         * Shell script (OSX, Linux).
         */
        SH(prefix = "#!/bin/sh\n", suffix = "\nread -s -n 1 -p \"Press any key to continue . . .\"", extension = "sh"),

        /**
         * Batch script (Windows).
         */
        BAT(prefix = "", suffix = "\nPAUSE", extension = "bat")
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