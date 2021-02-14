package eu.iamgio.pikt.properties

import java.io.File
import kotlin.system.exitProcess

/**
 * Storage for properties passed from command line.
 *
 * @param source source image file ("-Dsource")
 * @param output output executable file without extension ("-Doutput")
 * @param target compilation target ("-Dtarget")
 * @param jvmCompilerPath optional path to the Kotlin/JVM compiler (required if [target] is [CompilationTarget.JVM]) ("-Djvmcompiler")
 * @param nativeCompilerPath optional path to the Kotlin/Native compiler (required if [target] is [CompilationTarget.NATIVE]) ("-Dnativecompiler")
 * @param colors colors scheme ("-Dcolors")
 * @author Giorgio Garofalo
 */
data class PiktProperties(
        val source: File,
        val output: File,
        val target: CompilationTarget,
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
        val outputProperty             = System.getProperty("source")
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
        } else File(sourceProperty).also {
            if(!it.exists()) {
                error("Source file $it does not exist.")
            }
        }

        // Output file without extension
        val output: File? = when {
            outputProperty != null -> File(outputProperty)
            source != null -> File(source.parent, source.nameWithoutExtension)
            else -> null
        }

        // Compilation target type
        val target = CompilationTarget.values().firstOrNull { it.argName == targetProperty }
        if(target == null) {
            error("Compilation target $target is invalid. 'jvm' and 'native' are supported targets.")
        }

        // JVM compiler
        if(jvmCompilerPathProperty == null) {
            if(CompilationTarget.JVM == target) {
                error("JVM compiler (-Djvmcompiler) is not set but target is JVM.")
            }
        } else if(!File(jvmCompilerPathProperty).exists()) {
            error("JVM compiler $jvmCompilerPathProperty does not exist.")
        }

        // Native compiler
        if(nativeCompilerPathProperty == null) {
            if(CompilationTarget.NATIVE == target) {
                error("Native compiler (-Dnativecompiler) is not set but target is native.")
            }
        } else if(!File(nativeCompilerPathProperty).exists()) {
            error("Native compiler $nativeCompilerPathProperty does not exist.")
        }

        // Colors scheme
        val colorsPropertiesRetriever = ColorsPropertiesRetriever()
        if(colorsProperty != null && File(colorsProperty).exists()) {
            colorsPropertiesRetriever.loadProperties(colorsProperty)
        } else if(!isError) {
            println("Colors scheme not found. Using the default one.")
            println("Run Pikt with the -createcolors <name> argument to create a scheme.")
        }

        if(isError) {
            System.err.println("Could not initialize Pikt properties. Exiting.")
            exitProcess(-1)
        }

        return PiktProperties(
                source = source!!,
                output = output!!,
                target = target!!,
                jvmCompilerPath = jvmCompilerPathProperty,
                nativeCompilerPath = nativeCompilerPathProperty,
                colors = colorsPropertiesRetriever.retrieve()
        )
    }
}