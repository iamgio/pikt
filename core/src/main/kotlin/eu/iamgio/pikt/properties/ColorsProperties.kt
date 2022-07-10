package eu.iamgio.pikt.properties

import eu.iamgio.pikt.lib.JarLibrary
import eu.iamgio.pikt.lib.Libraries
import java.io.InputStream
import java.io.InputStreamReader

const val INTERNAL_COLORS_SCHEME_PATH = "/colors.properties"

/**
 * Storage for properties, loaded from a .properties file, that define the "keywords" of the language.
 * A standard file can be created by running Pikt with the -createscheme argument.
 * The fields of this class refer to a hexadecimal color.
 *
 * @param whitespace ignored color
 * @param keywords keywords and statements
 * @param lambda lambda/code blocks open/close values
 * @param boolean boolean values
 * @param operators operators
 * @param libraries members (functions, variables, etc.) of external libraries as a name=hex map to be dynamically replaced
 * @param rawProperties raw Java properties data
 * @see ColorsProperties
 * @author Giorgio Garofalo
 */
data class ColorsProperties(
        val whitespace: ColorsProperty,
        val keywords: KeywordsColorsProperties,
        val lambda: LambdaColorsProperties,
        val boolean: BooleanColorsProperties,
        val operators: OperatorColorsProperties,
        val libraries: Map<String, ColorsProperty>,
        val rawProperties: java.util.Properties
) : Properties

/**
 * Color scheme for keywords and statements.
 *
 * @param setVariable define and set variables
 * @param functionCall call a function/method without catching its output value
 * @param if run code if a certain condition is verified
 * @param else runs if the previous if statement did not run
 * @param struct creates a struct
 * @param forEach runs a task for every element of the collection
 * @param print prints an expression or value out
 * @author Giorgio Garofalo
 */
data class KeywordsColorsProperties(
        val setVariable: ColorsProperty,
        val functionCall: ColorsProperty,
        val `if`: ColorsProperty,
        val `else`: ColorsProperty,
        val struct: ColorsProperty,
        val forEach: ColorsProperty,
        val `return`: ColorsProperty,
        val print: ColorsProperty
)

/**
 * Color scheme for boolean values.
 *
 * @param boolTrue boolean value 'true'
 * @param boolFalse boolean value 'false'
 * @author Giorgio Garofalo
 */
data class BooleanColorsProperties(
        val boolTrue: ColorsProperty,
        val boolFalse: ColorsProperty
): Properties

/**
 * Color scheme for lambdas.
 *
 * @param open block open
 * @param close block close
 * @author Giorgio Garofalo
 */
data class LambdaColorsProperties(
        val open: ColorsProperty,
        val close: ColorsProperty
): Properties

/**
 * Color scheme for operators.
 *
 * @param dot .
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
        val dot: ColorsProperty,
        val plus: ColorsProperty,
        val minus: ColorsProperty,
        val times: ColorsProperty,
        val divide: ColorsProperty,
        val modulo: ColorsProperty,
        val and: ColorsProperty,
        val or: ColorsProperty,
        val equality: ColorsProperty,
        val inequality: ColorsProperty,
        val greater: ColorsProperty,
        val greaterOrEquals: ColorsProperty,
        val less: ColorsProperty,
        val lessOrEquals: ColorsProperty
): Properties

/**
 * Class that parses JVM properties into a [PiktProperties] instance.
 * @param libraries external libraries
 *
 * @author Giorgio Garofalo
 */
class ColorsPropertiesRetriever(private val libraries: List<JarLibrary>) : PropertiesRetriever<ColorsProperties> {

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
    fun get(key: String): ColorsProperty {
        return if(key in properties.keys) {
            properties.getProperty(key)
        } else {
            internalProperties.getProperty(key)
        }.let { value -> ColorsProperty.of(value) }
    }

    /**
     * Loads [internalProperties], in case [properties] is missing properties.
     * Internal properties consist of the internal color scheme, along with libraries'.
     */
    private fun loadInternalProperties() {
        internalProperties.load(InputStreamReader(javaClass.getResourceAsStream(INTERNAL_COLORS_SCHEME_PATH)!!))
        libraries.forEach { library ->
            val scheme = library.colorScheme
            scheme?.properties?.forEach { key, value ->
                internalProperties.setProperty(library.info.getFullKey(key.toString()), value.toString())
            }
        }
    }

    /**
     * Converts values specified by a .properties file to parsed [ColorsProperties].
     * @return parsed properties
     */
    override fun retrieve(): ColorsProperties {
        loadInternalProperties()

        return ColorsProperties(
                get("whitespace"),
                KeywordsColorsProperties(
                        get("variable.set"),
                        get("funcall"),
                        get("if"),
                        get("else"),
                        get("struct"),
                        get("foreach"),
                        get("return"),
                        get("print")
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
                        get("op.dot"),
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
                libraries = Libraries.generateColorProperties(internalProperties.keys, ::get),
                properties
        )
    }
}