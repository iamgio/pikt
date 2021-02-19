package eu.iamgio.pikt.properties

import eu.iamgio.pikt.eval.StdLib
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Storage for properties, loaded from a .properties file, that define the "keywords" of the language.
 * A standard file can be created by running Pikt with the -createcolors=<name> argument.
 * The fields of this class refer to a hexadecimal color.
 *
 * @param whitespace ignored color
 * @param keywords keywords and statements
 * @param lambda lambda/code blocks open/close values
 * @param boolean boolean values
 * @param operators operators
 * @param stdlib members (functions, variables, etc.) of the standard library as a name=hex map to be dynamically replaced
 * @author Giorgio Garofalo
 */
data class ColorsProperties(
        val whitespace: String,
        val keywords: KeywordsColorsProperties,
        val lambda: LambdaColorsProperties,
        val boolean: BooleanColorsProperties,
        val operators: OperatorColorsProperties,
        val stdlib: Map<String, String>
) : Properties

/**
 * Color scheme for keywords and statements.
 *
 * @param defineVariable define variables
 * @param setVariable set variables value
 * @param methodCall call a method/function without catching the resource value
 * @param `if` run code if a certain condition is verified
 * @param `else` runs if the previous if statement did not run
 * @param forEach runs a task for every element of the collection
 * @author Giorgio Garofalo
 */
data class KeywordsColorsProperties(
        val defineVariable: String,
        val setVariable: String,
        val methodCall: String,
        val `if`: String,
        val `else`: String,
        val forEach: String
)

/**
 * Color scheme for boolean values.
 *
 * @param boolTrue boolean value 'true'
 * @param boolFalse boolean value 'false'
 * @author Giorgio Garofalo
 */
data class BooleanColorsProperties(
        val boolTrue: String,
        val boolFalse: String
): Properties

/**
 * Color scheme for lambdas.
 *
 * @param open block open
 * @param close block close
 * @author Giorgio Garofalo
 */
data class LambdaColorsProperties(
        val open: String,
        val close: String
): Properties

/**
 * Color scheme for operators.
 *
 * @param plus +
 * @param minus -
 * @param times *
 * @param divide /
 * @param modulo %
 * @param and &&
 * @param or ||
 * @param equality ==
 * @param inequality !=
 * @param greater >
 * @param greaterOrEquals >=
 * @param less <
 * @param lessOrEquals <=
 * @author Giorgio Garofalo
 */
data class OperatorColorsProperties(
        val plus: String,
        val minus: String,
        val times: String,
        val divide: String,
        val modulo: String,
        val and: String,
        val or: String,
        val equality: String,
        val inequality: String,
        val greater: String,
        val greaterOrEquals: String,
        val less: String,
        val lessOrEquals: String
): Properties

/**
 * Class that parses JVM properties into a [PiktProperties] instance.
 *
 * @author Giorgio Garofalo
 */
class ColorsPropertiesRetriever : PropertiesRetriever<ColorsProperties> {

    /**
     * External properties.
     */
    private val properties = java.util.Properties()

    /**
     * Internal properties used to fill missing properties.
     */
    private val internalProperties = java.util.Properties()

    /**
     * Loads external properties.
     * @param inputStream .properties input stream
     */
    fun loadProperties(inputStream: InputStream) {
        properties.load(inputStream)
    }

    /**
     * Gets the value paired with [key] from the external [properties]. If the value is missing, it gets it from [internalProperties].
     * @return corresponding hex value
     */
    fun get(key: String): String {
        return if(key in properties.keys) {
            properties.getProperty(key)
        } else {
            internalProperties.getProperty(key)
        }
    }

    /**
     * Converts values specified by a .properties file to parsed [ColorsProperties].
     * @return parsed properties
     */
    override fun retrieve(): ColorsProperties {
        internalProperties.load(InputStreamReader(javaClass.getResourceAsStream("/properties/colors.properties")))

        return ColorsProperties(
                get("whitespace"),
                KeywordsColorsProperties(
                        get("variable.define"),
                        get("variable.set"),
                        get("methodcall"),
                        get("if"),
                        get("else"),
                        get("foreach")
                ),
                LambdaColorsProperties(
                        get("lambda.open"),
                        get("lambda.close"),
                ),
                BooleanColorsProperties(
                        get("bool.true"),
                        get("bool.false")
                ),
                OperatorColorsProperties(
                        get("op.plus"),
                        get("op.minus"),
                        get("op.times"),
                        get("op.divide"),
                        get("op.modulo"),
                        get("op.and"),
                        get("op.or"),
                        get("op.equality"),
                        get("op.inequality"),
                        get("op.greater"),
                        get("op.greater_or_equals"),
                        get("op.less"),
                        get("op.less_or_equals")
                ),
                stdlib = StdLib.generateColorProperties(internalProperties.keys) { key -> get(key) }
        )
    }
}