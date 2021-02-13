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
 * // TODO colors properties
 * @author Giorgio Garofalo
 */
data class PiktProperties(
        val source: File,
        val output: File,
        val target: CompilationTarget,
        val jvmCompilerPath: String?,
        val nativeCompilerPath: String?,
)

/**
 * Single-instance object that parses JVM properties into a [PiktProperties] instance
 *
 * @author Giorgio Garofalo
 */
object PiktPropertiesRetriever {

    /**
     * Converts raw JVM properties to parsed [PiktProperties].
     * If any required field is missing or invalid it prints an error and exits.
     * @return parsed properties
     */
    fun retrieve(): PiktProperties {
        val sourceProperty             = System.getProperty("source")
        val outputProperty             = System.getProperty("source")
        val targetProperty             = System.getProperty("target")
        val jvmCompilerPathProperty    = System.getProperty("jvmcompiler")
        val nativeCompilerPathProperty = System.getProperty("nativecompiler")

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
        if(jvmCompilerPathProperty == null && CompilationTarget.JVM == target) {
            error("JVM compiler (-Djvmcompiler) is not set but target is JVM.")
        } else if(!File(jvmCompilerPathProperty).exists()) {
            error("JVM compiler $jvmCompilerPathProperty does not exist.")
        }

        // Native compiler
        if(nativeCompilerPathProperty == null && CompilationTarget.NATIVE == target) {
            error("Native compiler (-Dnativecompiler) is not set but target is native.")
        } else if(!File(nativeCompilerPathProperty).exists()) {
            error("Native compiler $nativeCompilerPathProperty does not exist.")
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
        )
    }
}