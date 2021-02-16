package eu.iamgio.pikt.properties

import eu.iamgio.pikt.compiler.CompilationTarget
import eu.iamgio.pikt.compiler.isAny
import eu.iamgio.pikt.compiler.isAnyNative
import eu.iamgio.pikt.compiler.isAnyNull
import java.io.File
import kotlin.system.exitProcess

/**
 * Storage for properties passed from command line.
 *
 * @param source source image file ("-Dsource")
 * @param output output executable file without extension ("-Doutput")
 * @param targets compilation targets ("-Dtarget")
 * @param jvmCompilerPath optional path to the Kotlin/JVM compiler (required if any of [targets] is [CompilationTarget.JVM]) ("-Djvmcompiler")
 * @param nativeCompilerPath optional path to the Kotlin/Native compiler (required if any of [targets] is native) ("-Dnativecompiler")
 * @param colors color scheme ("-Dcolors")
 * @author Giorgio Garofalo
 */
data class PiktProperties(
        val source: File,
        val output: String,
        val targets: List<CompilationTarget>,
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
     * Converts raw JVM properties to parsed [PiktProperties].
     * If any required field is missing or invalid it prints an error and exits.
     * @return parsed properties
     */
    override fun retrieve(): PiktProperties {
        val sourceProperty             = System.getProperty("source")
        val outputProperty             = System.getProperty("output")
        val targetProperty             = System.getProperty("target")
        val jvmCompilerPathProperty    = System.getProperty("jvmcompiler")
        val nativeCompilerPathProperty = System.getProperty("nativecompiler")
        val colorsProperty             = System.getProperty("colors")

        var isError = false

        fun error(message: String) {
            System.err.println(message)
            isError = true
        }

        // Source image file
        val source: File? = if(sourceProperty == null) {
            error("Source file (-Dsource) is not set.")
            null
        } else File(sourceProperty).absoluteFile.also {
            if(!it.exists()) {
                error("Source file $it does not exist.")
            }
        }

        // Output name
        val output: String = when {
            outputProperty != null -> outputProperty
            source != null -> source.nameWithoutExtension
            else -> "out"
        }

        // Compilation targets
        val targets = targetProperty.split(",").map { targetArg ->
            CompilationTarget.values().firstOrNull { it.argName == targetArg }
        }
        if(targets.isAnyNull()) {
            error("One or more compilation target are null.")
        }

        // JVM compiler
        if(jvmCompilerPathProperty == null) {
            if(targets.isAny(CompilationTarget.JVM)) {
                error("JVM compiler (-Djvmcompiler) is not set but at least one target is JVM.")
            }
        } else if(!File(jvmCompilerPathProperty).exists()) {
            error("JVM compiler $jvmCompilerPathProperty does not exist.")
        }

        // Native compiler
        if(nativeCompilerPathProperty == null) {
            if(targets.isAnyNative()) {
                error("Native compiler (-Dnativecompiler) is not set but at least target is native.")
            }
        } else if(!File(nativeCompilerPathProperty).exists()) {
            error("Native compiler $nativeCompilerPathProperty does not exist.")
        }

        // Color scheme
        val colorsPropertiesRetriever = ColorsPropertiesRetriever()
        if(colorsProperty != null && File(colorsProperty).exists()) {
            colorsPropertiesRetriever.loadProperties(colorsProperty)
        } else if(!isError) {
            println("Color scheme not found. Using the default one.")
            println("Run Pikt with the -createcolors=name argument to create a scheme.\n")
        }

        if(isError) {
            System.err.println("Could not initialize Pikt properties. Exiting.")
            exitProcess(-1)
        }

        return PiktProperties(
                source = source!!,
                output = output,
                targets = targets.filterNotNull(),
                jvmCompilerPath = jvmCompilerPathProperty,
                nativeCompilerPath = nativeCompilerPathProperty,
                colors = colorsPropertiesRetriever.retrieve()
        )
    }
}