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
         * Command script (OSX) based on Bash.
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

/**
 * Finds out script types and content for [this] target.
 * @param executableName output name without extension
 * @return array of scripts
 */
fun CompilationTarget.getStarterScriptFiles(executableName: String): Array<StarterScriptFile> {
    return when(this) {
        CompilationTarget.JVM -> {
            "java -jar $executableName.jar".let { command ->
                arrayOf(
                        StarterScriptFile(StarterScriptFile.Type.SH, command),
                        StarterScriptFile(StarterScriptFile.Type.BAT, command),
                        StarterScriptFile(StarterScriptFile.Type.COMMAND, "java -jar \"\$(dirname \"\$BASH_SOURCE\")\"/$executableName.jar")
                )
            }
        }
        CompilationTarget.NATIVE_WINDOWS -> {
            arrayOf(StarterScriptFile(StarterScriptFile.Type.BAT, "$executableName.exe"))
        }
        CompilationTarget.NATIVE_LINUX -> {
            arrayOf(StarterScriptFile(StarterScriptFile.Type.SH, "./$executableName.kexe"))
        }
        CompilationTarget.NATIVE_OSX -> {
            arrayOf(StarterScriptFile(StarterScriptFile.Type.COMMAND, "\"\$(dirname \"\$BASH_SOURCE\")\"/$executableName.kexe"))
        }
    }
}