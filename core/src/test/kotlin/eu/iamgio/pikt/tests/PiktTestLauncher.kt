package eu.iamgio.pikt.tests

import eu.iamgio.pikt.compiler.AbstractInterpreter
import eu.iamgio.pikt.compiler.CompilationTarget
import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.ColorsPropertiesRetriever
import eu.iamgio.pikt.properties.PiktProperties
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.registerStatements
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO


/**
 * Launcher for Pikt tests.
 *
 * @author Giorgio Garofalo
 */
abstract class PiktTestLauncher {

    protected val tempDirectory = File(System.getProperty("java.io.tmpdir") + File.separator + "pikt-test")

    init {
        tempDirectory.mkdir()
        registerStatements()
    }

    /**
     * @param name name of the source image
     * @return source image as [InputStream]
     */
    abstract fun getImage(name: String): InputStream

    /**
     * @param colorSchemeName name of the color scheme
     * @return color scheme as [InputStream]
     */
    abstract fun getColorScheme(colorSchemeName: String): InputStream

    /**
     * Launches the interpreter and returns all the non-error messages.
     * @param name PNG image name without extension
     * @param colorSchemeName name of the optional .properties color scheme
     * @return non-error lines in order
     */
    fun launch(name: String, colorSchemeName: String? = null): List<String> {
        println("Launching test $name")

        val propertiesRetriever = PiktPropertiesRetriever()
        val libraries = propertiesRetriever.libraries()
        val jvmCompiler = propertiesRetriever.jvmCompiler()
        propertiesRetriever.checkCompilers(jvmCompiler, null, targets = listOf(CompilationTarget.JVM))

        if(propertiesRetriever.isError) {
            throw IllegalStateException("Failed to initialize Pikt properties.")
        }

        val properties = PiktProperties(
                source = File(tempDirectory, "ignored"),
                output = "out",
                compilationTargets = emptyList(),
                libraries = libraries,
                jvmCompilerPath = jvmCompiler,
                nativeCompilerPath = null,
                colors = ColorsPropertiesRetriever(libraries).also {
                    if(colorSchemeName != null) {
                        it.loadProperties(getColorScheme(colorSchemeName))
                    }
                }.retrieve()
        )

        val lines = mutableListOf<String>()

        val image = PiktImage(ImageIO.read(getImage(name)), properties.colors)

        val evaluator = Evaluator()
        evaluator.evaluate(image, properties.libraries)

        println(evaluator.outputCode)

        val interpreter = object : AbstractInterpreter(evaluator, properties) {
            override fun printProcessLine(line: String, isError: Boolean) {
                if(isError) {
                    System.err.println(line)
                } else {
                    println("[$name] $line")
                    lines.add(line)
                }
            }
        }

        interpreter.compile()

        return lines
    }
}