package eu.iamgio.pikt.properties

import eu.iamgio.pikt.compiler.CompilationTarget
import eu.iamgio.pikt.compiler.isAnyJVM
import eu.iamgio.pikt.compiler.isAnyNative
import eu.iamgio.pikt.compiler.isAnyNull
import eu.iamgio.pikt.lib.JarLibrary
import java.io.File
import java.io.FileInputStream
import kotlin.system.exitProcess

/**
 * Storage for properties passed from command line.
 *
 * @param source source image file ("-Dsource")
 * @param output output executable file without extension ("-Doutput")
 * @param compilationTargets compilation targets ("-Dtarget")
 * @param stdlib standard library JAR library
 * @param jvmCompilerPath optional path to the Kotlin/JVM compiler (required if any of [compilationTargets] is [CompilationTarget.JVM]) ("-Djvmcompiler")
 * @param nativeCompilerPath optional path to the Kotlin/Native compiler (required if any of [compilationTargets] is native) ("-Dnativecompiler")
 * @param colors color scheme ("-Dcolors")
 * @author Giorgio Garofalo
 */
data class PiktProperties(
        val source: File,
        val output: String,
        val compilationTargets: List<CompilationTarget>,
        val stdlib: JarLibrary,
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
    var isError = false
        private set

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
        // Source image file
        val source = source()

        // Output name
        val output = output(source = source)

        // Compilation targets
        val targets = compilationTargets()

        // Standard library
        val stdlib = stdlib()

        val jvmCompilerPath = jvmCompiler()
        val nativeCompilerPath = nativeCompiler()

        checkCompilers(jvmCompilerPath, nativeCompilerPath, targets)

        // Color scheme
        val colorsPropertiesRetriever = colors()

        if(isError) {
            System.err.println("Could not initialize Pikt properties. Exiting.")
            exitProcess(-1)
        }

        return PiktProperties(
                source = source!!,
                output = output,
                compilationTargets = targets,
                stdlib = stdlib,
                jvmCompilerPath = jvmCompilerPath,
                nativeCompilerPath = nativeCompilerPath,
                colors = colorsPropertiesRetriever.retrieve()
        )
    }

    /**
     * Gets the source image file from a given path.
     */
    private fun source(sourceProperty: String? = System.getProperty("source")) =
            if(sourceProperty == null) {
                error("Source file (-Dsource) is not set.")
                null
            } else File(sourceProperty).absoluteFile.also {
                if(!it.exists()) {
                    error("Source file $it does not exist.")
                }
            }

    /**
     * Gets the output executable name from a property if given, uses source name otherwise.
     */
    private fun output(outputProperty: String? = System.getProperty("output"), source: File?) = when {
        outputProperty != null -> outputProperty
        source != null -> source.nameWithoutExtension
        else -> "out"
    }

    /**
     * Gets the compilation targets from a given property, split by commas.
     */
    private fun compilationTargets(compilationTargetsProperty: String? = System.getProperty("targets")): List<CompilationTarget> {
        val targets = compilationTargetsProperty?.split(",")?.map { targetArg ->
            CompilationTarget.values().firstOrNull { it.argName == targetArg }
        } ?: emptyList()
        if(targets.isAnyNull()) {
            error("One or more compilation target are null.")
        }
        return targets.filterNotNull()
    }

    /**
     * Gets the standard library JAR from a property if given, uses ./stdlib.jar otherwise.
     */
    fun stdlib(stdlibProperty: String? = System.getProperty("stdlib")): JarLibrary {
        return JarLibrary(File(stdlibProperty ?: "stdlib.jar").also {
            if(!it.exists()) {
                error("stdlib (-Dstdlib) does not exist at $it.")
            }
        })
    }

    /**
     * Gets the color scheme from a given property.
     */
    private fun colors(colorsProperty: String? = System.getProperty("colors")): ColorsPropertiesRetriever {
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

    /**
     * Gets the JVM compiler from a given property (without running checks).
     */
    fun jvmCompiler(jvmCompilerPathProperty: String? = System.getProperty("jvmcompiler")) = jvmCompilerPathProperty

    /**
     * Gets the Native compiler from a given property (without running checks).
     */
    private fun nativeCompiler(nativeCompilerPathProperty: String? = System.getProperty("nativecompiler")) = nativeCompilerPathProperty

    /**
     * Checks for compilers' existance based on active compilation [targets].
     */
    fun checkCompilers(jvmCompilerPath: String?, nativeCompilerPath: String?, targets: List<CompilationTarget>) {
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