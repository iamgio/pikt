package eu.iamgio.pikt.tests

import eu.iamgio.pikt.compiler.kotlin.KotlinInterpreter
import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.log.Log
import eu.iamgio.pikt.properties.PiktProperties
import java.io.OutputStream

/**
 * The interpreter used for internal tests.
 *
 * @param name test name
 * @param evaluator code evaluator
 * @param properties Pikt properties
 * @author Giorgio Garofalo
 */
class PiktTestInterpreter(
    private val name: String,
    evaluator: Evaluator,
    properties: PiktProperties
) : KotlinInterpreter(evaluator, properties) {

    /**
     * Standard output lines.
     */
    val lines = mutableListOf<String>()

    override fun handleInput(stdin: OutputStream) {
        // There could be a default input value in the future.
    }

    override fun printProcessLine(line: String, isError: Boolean) {
        if(isError) {
            Log.error(line)
        } else {
            Log.info("[$name] $line")
            lines += line
        }
    }
}