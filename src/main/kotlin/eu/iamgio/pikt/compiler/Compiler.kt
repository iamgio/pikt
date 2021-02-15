package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.properties.PiktProperties
import java.io.File

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
        val kotlinFile = File(properties.output.parent, properties.output.nameWithoutExtension + ".kt")
        kotlinFile.writeText(kotlinCode)

        println("Compiling. Please wait...")

        val command = properties.target.compilerCommand(kotlinFile, properties)
        val process = Runtime.getRuntime().exec(command)

        println(process.inputStream.readBytes().decodeToString())
        process.errorStream.readBytes().decodeToString()
                .lines()
                .filter { !it.startsWith("WARNING") }
                .forEach { println(it) }

        kotlinFile.delete()
    }
}