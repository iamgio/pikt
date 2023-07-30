package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktProperties
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.Reader
import kotlin.concurrent.thread

/**
 * Compiles code and executes it on the fly.
 *
 * @param evaluator evaluator containing output code
 * @param properties Pikt properties
 * @author Giorgio Garofalo
 */
abstract class Interpreter(evaluator: Evaluator, properties: PiktProperties) : AbstractInterpreter(evaluator, properties) {

    private var hasError = false

    private var stdinReader: Reader? = null

    /**
     * Header message shown before error messages.
     */
    abstract val errorMessageHeader: String

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
            } catch (e: IOException) {
                Log.error("An error occurred while reading input:")
                e.printStackTrace()
            }
        }
    }

    override fun printProcessLine(line: String, isError: Boolean) {
        if (isError) {
            if (!hasError) {
                hasError = true
                Log.error(this.errorMessageHeader)
            }
            Log.error(line)
        } else {
            Log.info(line)
        }
    }
}
