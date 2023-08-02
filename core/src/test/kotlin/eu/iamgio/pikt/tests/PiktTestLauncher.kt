package eu.iamgio.pikt.tests

import eu.iamgio.pikt.compiler.CompilationTarget
import eu.iamgio.pikt.eval.Scope
import eu.iamgio.pikt.eval.kotlin.KotlinEvaluator
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.ColorsPropertiesRetriever
import eu.iamgio.pikt.properties.PiktProperties
import eu.iamgio.pikt.properties.PiktPropertiesRetriever
import eu.iamgio.pikt.properties.SystemPropertyConstants
import eu.iamgio.pikt.registerStatements
import eu.iamgio.pikt.statement.kotlin.KotlinStatementFactory
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
        registerStatements(KotlinStatementFactory())

        System.setProperty(SystemPropertyConstants.THROW_EXCEPTION_ON_READER_ERROR, "")
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
        Log.info("Launching test $name")

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

        val image = PiktImage(ImageIO.read(getImage(name)), properties.colors)

        val evaluator = KotlinEvaluator()
        evaluator.evaluate(image, Scope.buildMainScope(properties.libraries, properties.colors.libraries))

        Log.info(evaluator.outputCode)

        val interpreter = PiktTestInterpreter(name, evaluator, properties)
        interpreter.compile()

        return interpreter.lines
    }
}