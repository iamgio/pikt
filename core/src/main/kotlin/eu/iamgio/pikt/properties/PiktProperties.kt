package eu.iamgio.pikt.properties

import eu.iamgio.pikt.compiler.CompilationTarget
import eu.iamgio.pikt.compiler.isAnyJVM
import eu.iamgio.pikt.compiler.isAnyNative
import eu.iamgio.pikt.compiler.isAnyNull
import java.io.File
import java.io.FileInputStream
import kotlin.system.exitProcess

/**
 * Storage for properties passed from command line.
 *
 * @param source source image file ("-Dsource")
 * @param output output executable file without extension ("-Doutput")
 * @param compilationTargets compilation targets ("-Dtarget")
 * @param stdlib standard library JAR file
 * @param jvmCompilerPath optional path to the Kotlin/JVM compiler (required if any of [compilationTargets] is [CompilationTarget.JVM]) ("-Djvmcompiler")
 * @param nativeCompilerPath optional path to the Kotlin/Native compiler (required if any of [compilationTargets] is native) ("-Dnativecompiler")
 * @param colors color scheme ("-Dcolors")
 * @author Giorgio Garofalo
 */
data class PiktProperties(
        val source: File,
        val output: String,
        val compilationTargets: List<CompilationTarget>,
        val stdlib: File,
        val jvmCompilerPath: String?,
        val nativeCompilerPath: String?,
        val colors: ColorsProperties
) : Properties

/**
 * Class that parses JVM properties into a [PiktProperties] instance.
 *
 * @author Giorgio Garofalo
 */
class PiktPropertiesRetriever : PropertiesRetriever<PiktProperties> {

    /**
     * Whether an error during the parsing process occurred.
     */
    private var isError = false

    private fun error(message: String) {
        System.err.println(message)
        isError = true
    }

    /**
     * Converts raw JVM properties to parsed [PiktProperties].
     * If any required field is missing or invalid it prints an error and exits.
     * @return parsed properties
     */
    override fun retrieve(): PiktProperties {
        val sourceProperty             = System.getProperty("source")
        val outputProperty             = System.getProperty("output")
        val compTargetsProperty        = System.getProperty("targets")
        val stdlibProperty             = System.getProperty("stdlib")
        val jvmCompilerPathProperty    = System.getProperty("jvmcompiler")
        val nativeCompilerPathProperty = System.getProperty("nativecompiler")
        val colorsProperty             = System.getProperty("colors")

        // Source image file
        val source = source(sourceProperty)

        // Output name
        val output = output(outputProperty, source)

        // Compilation targets
        val targets = compilationTargets(compTargetsProperty)

        // Standard library
        val stdlib = stdlib(stdlibProperty)

        checkCompilers(jvmCompilerPathProperty, nativeCompilerPathProperty, targets)

        // Color scheme
        val colorsPropertiesRetriever = colors(colorsProperty)

        if(isError) {
            System.err.println("Could not initialize Pikt properties. Exiting.")
            exitProcess(-1)
        }

        return PiktProperties(
                source = source!!,
                output = output,
                compilationTargets = targets,
                stdlib = stdlib,
                jvmCompilerPath = jvmCompilerPathProperty,
                nativeCompilerPath = nativeCompilerPathProperty,
                colors = colorsPropertiesRetriever.retrieve()
        )
    }

    private fun source(sourceProperty: String?) =
            if(sourceProperty == null) {
                error("Source file (-Dsource) is not set.")
                null
            } else File(sourceProperty).absoluteFile.also {
                if(!it.exists()) {
                    error("Source file $it does not exist.")
                }
            }

    private fun output(outputProperty: String?, source: File?) = when {
        outputProperty != null -> outputProperty
        source != null -> source.nameWithoutExtension
        else -> "out"
    }

    private fun compilationTargets(compilationTargetsProperty: String?): List<CompilationTarget> {
        val targets = compilationTargetsProperty?.split(",")?.map { targetArg ->
            CompilationTarget.values().firstOrNull { it.argName == targetArg }
        } ?: emptyList()
        if(targets.isAnyNull()) {
            error("One or more compilation target are null.")
        }
        return targets.filterNotNull()
    }

    private fun stdlib(stdlibProperty: String?): File {
        return File(stdlibProperty ?: "stdlib.jar").also {
            if(!it.exists()) {
                error("stdlib (-Dstdlib) does not exist at $it.")
            }
        }
    }

    private fun colors(colorsProperty: String?): ColorsPropertiesRetriever {
        val retriever = ColorsPropertiesRetriever()
        val colorsFile = File("$colorsProperty.properties")
        if(colorsProperty != null && colorsFile.exists()) {
            retriever.loadProperties(FileInputStream(colorsFile))
        } else if(!isError) {
            println("Color scheme not found. Using the default one.")
            println("Run Pikt with the -createscheme=name argument to create a scheme.\n")
        }
        return retriever
    }

    private fun checkCompilers(jvmCompilerPath: String?, nativeCompilerPath: String?, targets: List<CompilationTarget>) {
        // JVM compiler
        if(jvmCompilerPath == null) {
            if(targets.isAnyJVM()) {
                error("JVM compiler (-Djvmcompiler) is not set but one target is JVM or interpretation is enabled.")
            }
        } else if(!File(jvmCompilerPath).exists()) {
            error("JVM compiler $jvmCompilerPath does not exist.")
        }

        // Native compiler
        if(nativeCompilerPath == null) {
            if(targets.isAnyNative()) {
                error("Native compiler (-Dnativecompiler) is not set but at least target is native.")
            }
        } else if(!File(nativeCompilerPath).exists()) {
            error("Native compiler $nativeCompilerPath does not exist.")
        }
    }
}