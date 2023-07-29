package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktProperties
import java.io.*
import kotlin.concurrent.thread

/**
 * Abstract interpreter that allows handling process streams.
 *
 * @param evaluator evaluator containing output code
 * @param properties Pikt properties
 * @author Giorgio Garofalo
 */
abstract class AbstractInterpreter(evaluator: Evaluator, properties: PiktProperties) : AbstractCompiler(evaluator, properties) {

    override val sourceFile = File(outputFolder, properties.output + ".kts")

    override fun applyEvaluatorSettings() {}

    override fun getTargets() = listOf(CompilationTarget.JVM)

    override fun onPreCompile(target: CompilationTarget) {}

    override fun generateCommand(target: CompilationTarget): Array<String> {
        return target.commandGenerator.generateInterpretCommand(sourceFile, properties)
    }

    override fun onPostCompile(target: CompilationTarget) {}
}

/**
 * Interprets Kotlin code.
 *
 * @param evaluator evaluator containing output code
 * @param properties Pikt properties
 * @author Giorgio Garofalo
 */
class Interpreter(evaluator: Evaluator, properties: PiktProperties) : AbstractInterpreter(evaluator, properties) {

    private var hasKotlinError = false

    private var stdinReader: Reader? = null

    override fun onPostCompile(target: CompilationTarget) {
        super.onPostCompile(target)
        stdinReader?.close()
    }

    override fun handleInput(stdin: OutputStream) {
        stdinReader = InputStreamReader(System.`in`)
        // We don't actually read from this reader, but without it (and `stdinReader.close()` above)
        // the execution waits for some input that is not needed and may result in an IOException.

        thread {
            try {
                var ret: Int
                while(System.`in`.read().also { ret = it } != -1) {
                    stdin.write(ret)
                    stdin.flush()
                }
            } catch(e: IOException) {
                Log.error("An error occurred while reading input:")
                e.printStackTrace()
            }
        }
    }

    override fun printProcessLine(line: String, isError: Boolean) {
        if(isError) {
            if(!hasKotlinError) {
                hasKotlinError = true
                Log.error(KOTLIN_COMPILER_ERROR_MESSAGE_HEADER)
            }
            Log.error(line)
        } else {
            Log.info(line)
        }
    }
}