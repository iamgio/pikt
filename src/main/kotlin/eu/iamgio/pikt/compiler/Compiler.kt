package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.eval.StdLib
import eu.iamgio.pikt.properties.PiktProperties
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader


/**
 * Compiles the generated [kotlinCode] to an executable.
 *
 * @param kotlinCode generated output Kotlin code
 * @param properties Pikt properties containing compilation information
 * @author Giorgio Garofalo
 */
class Compiler(private val kotlinCode: String, private val properties: PiktProperties) {

    /**
     * Generates the source .kt file and runs the Kotlin compiler
     */
    fun compile() {
        // Find output folder and create if absent
        val outputFolder = File(properties.source.parent, "out").also { it.mkdir() }

        // Generate temporary Kotlin source file
        val kotlinFile = File(outputFolder, properties.output + ".kt")

        // Compile for each target
        properties.targets.forEach { target ->
            // Append target-specific library to the code
            kotlinFile.writeText(kotlinCode + "\n" + StdLib.getTargetSpecificFile(target).readContent(null))

            // Find target folder and create if absent
            // Example: out/windows for target WINDOWS
            val targetFolder = File(outputFolder, target.argName).also { it.mkdir() }

            println("\nCompiling for target $target. Please wait...\n")

            // Fetch the command needed for the target to be compiled
            val command = target.compilerCommand(kotlinFile, File(targetFolder, properties.output), properties)

            // Execute the command
            val process = Runtime.getRuntime().exec(command)

            // Print the command output
            printStream(process.inputStream)
            printStream(process.errorStream)

            // Generate script (.sh and .bat) files
            getStarterScriptFiles(name = properties.output, target).forEach {
                it.create(targetFolder, name = properties.output)
            }
        }

        // Delete temporary Kotlin source file
        kotlinFile.delete()
    }

    /**
     * Finds out script types and content based on [target]
     * @param name output name without extension
     * @param target compilation target to find the script for
     * @return array of scripts
     */
    private fun getStarterScriptFiles(name: String, target: CompilationTarget): Array<StarterScriptFile> {
        return when(target) {
            CompilationTarget.JVM -> {
                "java -jar $name.jar".let { command ->
                    arrayOf(
                            StarterScriptFile(StarterScriptFile.Type.SH, command),
                            StarterScriptFile(StarterScriptFile.Type.BAT, command)
                    )
                }
            }
            CompilationTarget.NATIVE_WINDOWS -> {
                arrayOf(StarterScriptFile(StarterScriptFile.Type.BAT, "./$name.exe"))
            }
            CompilationTarget.NATIVE_OSX, CompilationTarget.NATIVE_LINUX -> {
                arrayOf(StarterScriptFile(StarterScriptFile.Type.SH, "./$name.kexe"))
            }
        }
    }

    /**
     * Reads and prints the content of the process [InputStream].
     */
    private fun printStream(inputStream: InputStream) {
        val reader = BufferedReader(InputStreamReader(inputStream))

        var line: String?

        while(reader.readLine().also { line = it } != null) {
            if(line!!.startsWith("WARNING").not()) println(">   $line")
        }
    }
}