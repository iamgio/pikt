package eu.iamgio.pikt.properties

import eu.iamgio.pikt.compiler.CompilationTarget
import eu.iamgio.pikt.compiler.isAnyJVM
import eu.iamgio.pikt.compiler.isAnyNative
import eu.iamgio.pikt.compiler.isAnyNull
import eu.iamgio.pikt.exit.ERROR_BAD_PROPERTIES
import eu.iamgio.pikt.exit.exit
import eu.iamgio.pikt.lib.Libraries
import eu.iamgio.pikt.lib.jar.JarLibraries
import eu.iamgio.pikt.lib.jar.JarLibrary
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.project.PiktProjectInfo
import java.io.File
import java.io.FileInputStream

/**
 * Storage for properties passed from command line.
 *
 * @param source source image file ("-Dsource")
 * @param output output executable file without extension ("-Doutput")
 * @param compilationTargets compilation targets ("-Dtarget")
 * @param libraries external libraries, including stdlib
 * @param jvmCompilerPath optional path to the Kotlin/JVM compiler (required if any of [compilationTargets] is [CompilationTarget.JVM]) ("-Djvmcompiler")
 * @param nativeCompilerPath optional path to the Kotlin/Native compiler (required if any of [compilationTargets] is native) ("-Dnativecompiler")
 * @param colors color scheme ("-Dcolors")
 * @author Giorgio Garofalo
 */
data class PiktProperties(
        val source: File,
        val output: String,
        val compilationTargets: List<CompilationTarget>,
        val libraries: Libraries,
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
        Log.error(message)
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

        // JAR libraries
        val libraries = libraries()

        val jvmCompilerPath = jvmCompiler()
        val nativeCompilerPath = nativeCompiler()

        checkCompilers(jvmCompilerPath, nativeCompilerPath, targets)

        // Color scheme
        val colorsPropertiesRetriever = colors(libraries = libraries)

        if(isError) {
            exit(ERROR_BAD_PROPERTIES, message = "Could not initialize Pikt properties.")
        }

        return PiktProperties(
                source = source!!,
                output = output,
                compilationTargets = targets,
                libraries = libraries,
                jvmCompilerPath = jvmCompilerPath,
                nativeCompilerPath = nativeCompilerPath,
                colors = colorsPropertiesRetriever.retrieve()
        )
    }

    /**
     * Gets the source image file from a given path.
     */
    private fun source(sourceProperty: String? = System.getProperty(SystemPropertyConstants.SOURCE)) =
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
    private fun output(outputProperty: String? = System.getProperty(SystemPropertyConstants.OUTPUT), source: File?) = when {
        outputProperty != null -> outputProperty
        source != null -> source.nameWithoutExtension
        else -> "out"
    }

    /**
     * Gets the compilation targets from a given property, split by commas.
     */
    private fun compilationTargets(compilationTargetsProperty: String? = System.getProperty(SystemPropertyConstants.TARGETS)): List<CompilationTarget> {
        val targets = compilationTargetsProperty?.split(",")?.map { targetArg ->
            CompilationTarget.values().firstOrNull { it.argName == targetArg }
        } ?: emptyList()
        if(targets.isAnyNull()) {
            error("One or more compilation target are null.")
        }
        return targets.filterNotNull()
    }

    /**
     * Gets the standard library JAR from a property if given, `[./stdlib.jar]` otherwise.
     */
    fun libraries(librariesProperty: String? = System.getProperty(SystemPropertyConstants.LIBRARIES)): Libraries {
        val rawLibraries = librariesProperty?.split(",") ?: listOf("libraries/stdlib.jar")
        val jarLibraries = rawLibraries.map {
            val file = File(it)
            if(!file.exists()) {
                error("Library file $file (-${SystemPropertyConstants.LIBRARIES}) does not exist.")
            }
            JarLibrary(File(it))
        }

        // In the future there could be other types of libraries.
        return JarLibraries(jarLibraries)
    }

    /**
     * File of the optional color scheme file from a given property.
     */
    fun colorsFile(colorsProperty: String? = System.getProperty(SystemPropertyConstants.COLOR_SCHEME)): File? =
            colorsProperty?.let { File("$colorsProperty.properties") }

    /**
     * Gets the color scheme from a given property.
     */
    private fun colors(colorsFile: File? = colorsFile(), libraries: Libraries): ColorsPropertiesRetriever {
        val retriever = ColorsPropertiesRetriever(libraries)
        if(colorsFile != null && colorsFile.exists()) {
            retriever.loadProperties(FileInputStream(colorsFile))
        } else if(!isError) {
            Log.info("Color scheme not found. Using the default one.")
            Log.info("Run Pikt with the -createscheme argument to create a scheme.\n")
        }
        return retriever
    }

    /**
     * Gets the JVM compiler from a given property (without running checks).
     */
    fun jvmCompiler(jvmCompilerPathProperty: String? = System.getProperty(SystemPropertyConstants.JVM_COMPILER)) = jvmCompilerPathProperty

    /**
     * Gets the Native compiler from a given property (without running checks).
     */
    private fun nativeCompiler(nativeCompilerPathProperty: String? = System.getProperty(SystemPropertyConstants.NATIVE_COMPILER)) = nativeCompilerPathProperty

    /**
     * Checks for compilers' existance based on active compilation [targets].
     */
    fun checkCompilers(jvmCompilerPath: String?, nativeCompilerPath: String?, targets: List<CompilationTarget>) {
        // JVM compiler
        if(jvmCompilerPath == null) {
            if(targets.isAnyJVM()) {
                error("JVM compiler (-D${SystemPropertyConstants.JVM_COMPILER}) is not set but one target is JVM or interpretation is enabled.")
            }
        } else if(!File(jvmCompilerPath).exists()) {
            error("JVM compiler $jvmCompilerPath does not exist.")
        }

        // Native compiler
        if(nativeCompilerPath == null) {
            if(targets.isAnyNative()) {
                error("Native compiler (-D${SystemPropertyConstants.NATIVE_COMPILER}) is not set but at least target is native.")
            }
        } else if(!File(nativeCompilerPath).exists()) {
            error("Native compiler $nativeCompilerPath does not exist.")
        }
    }

    companion object {
        /**
         * @return project info file, if path (without extension) is set and if it exists
         * @see eu.iamgio.pikt.project.PiktProjectInfo
         */
        fun getProjectInfoFile(path: String? = System.getProperty(SystemPropertyConstants.PROJECT_INFO)): FileInputStream? {
            if(path == null) return null

            val file = File("$path.yml")
            if(!file.exists()) {
                exit(ERROR_BAD_PROPERTIES, message = "Project info file ${file.name} does not exist.")
            }
            return FileInputStream(file)
        }

        /**
         * @return selected project info task, if its name is set
         * @see eu.iamgio.pikt.project.PiktProjectInfo
         */
        fun getProjectInfoTaskFor(projectInfo: PiktProjectInfo, taskName: String? = System.getProperty(SystemPropertyConstants.PROJECT_INFO_TASK)): PiktProjectInfo? {
            if(taskName == null) return null
            return projectInfo.getTaskByName(taskName) ?: exit(ERROR_BAD_PROPERTIES, message = "Task $taskName does not exist.")
        }
    }
}