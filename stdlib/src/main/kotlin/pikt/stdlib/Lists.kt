@file:Suppress("UNCHECKED_CAST")

package pikt.stdlib

import pikt.error.PiktException
import pikt.error.PiktWrongArgumentTypeException
import pikt.error.ValueType.LIST
import pikt.error.ValueType.NUMBER
import pikt.error.ValueType.STRING
import pikt.error.ValueType.or

/**
 * Gets an iterable value from any nullable object value.
 * This is automatically inserted in for-each blocks.
 * @return an iterable if this object is a list or a string, a singleton list containing this object otherwise.
 */
@Suppress("UNCHECKED_CAST")
val Any?.iterable: Iterable<Any>
    get() = when(this) {
        null -> emptyList()
        is Iterable<*> -> this as Iterable<Any>
        is CharSequence -> toList()
        else -> kotlin.collections.listOf(this)
    }

/**
 * Creates a list of values.
 * @param items elements of the list
 * @return created list
 */
fun listOf(vararg items: Any): MutableList<Any> {
    return mutableListOf(*items)
}

/**
 * Creates an empty list.
 * @return empty list of objects
 */
fun listOf(): MutableList<Any> {
    return mutableListOf()
}

/**
 * @param list list or string to get the size from
 * @return size (length) of the [list], or `-1` if [list] is neither a list nor a string
 */
fun listSize(list: Any): Int {
    return when(list) {
        is List<*> -> list.size
        is CharSequence -> list.length
        else -> -1
    }
}

/**
 * @param list list or string to get the value from
 * @param index numeric index of the value within the list, from 0 (inclusive) to the size of the list (exclusive)
 */
fun listGetAt(list: Any, index: Any): Any {
    if(index !is Number) {
        throw PiktWrongArgumentTypeException(
                parameterName = "index",
                argumentValue = index,
                expectedType = NUMBER,
                reference = object {}
        )
    }
    if(index < 0 || index >= listSize(list)) {
        throw PiktException(
                "Invalid index: index = $index, size = ${listSize(list)}",
                reference = object {}
        )
    }
    val i = index.toInt()
    return when(list) {
        is List<*> -> list[i]
        is CharSequence -> list[i]
        else -> throw PiktWrongArgumentTypeException(
                parameterName = "list",
                argumentValue = list,
                expectedType = LIST or STRING,
                reference = object {}
        )
    }!!
}

/**
 * @param list list or string to get the value from
 * @param index numeric index of the value within the list, from 0 (inclusive) to the size of the list (exclusive)
 */
fun listSetAt(list: Any, index: Any, value: Any): Any {
    if(index !is Number) {
        throw PiktWrongArgumentTypeException(
                parameterName = "index",
                argumentValue = index,
                expectedType = NUMBER,
                reference = object {}
        )
    }
    if(index < 0 || index >= listSize(list)) {
        throw PiktException(
                "Invalid index: index = $index, size = ${listSize(list)}",
                reference = object {}
        )
    }
    val i = index.toInt()
    return when(list) {
        is MutableList<*> -> (list as MutableList<Any>)[i] = value
        is CharSequence -> list[i]
        else -> throw PiktWrongArgumentTypeException(
                parameterName = "list",
                argumentValue = list,
                expectedType = LIST or STRING,
                reference = object {}
        )
    }
}

/**
 * Adds values to a [list].
 * @param list list to be affected
 * @param items items to be added
 */
fun listAdd(list: Any, vararg items: Any) = when(list) {
    is MutableList<*> -> (list as MutableList<Any>).addAll(items)
    else -> throw PiktWrongArgumentTypeException(
            parameterName = "list",
            argumentValue = list,
            expectedType = LIST,
            reference = object {}
    )
}

/**
 * Removes values from a [list].
 * @param list list to be affected
 * @param items items to be removed
 */
fun listRemove(list: Any, vararg items: Any) = when(list) {
    is MutableList<*> -> (list as MutableList<Any>).removeAll(items)
    else -> throw PiktWrongArgumentTypeException(
            parameterName = "list",
            argumentValue = list,
            expectedType = LIST,
            reference = object {}
    )
}

/**
 * Generates a range of integers from [start] to [end].
 * @param start start number
 * @param end end number
 * @param forceUp whether the range should be ascending, even if [start] > [end]
 * @return list of integers from [start] to [end]
 */
@JvmOverloads
fun range(start: Any, end: Any, forceUp: Any = false): MutableList<Int> {
    if(start !is Number) {
        throw PiktWrongArgumentTypeException(
                parameterName = "start",
                argumentValue = start,
                expectedType = NUMBER,
                reference = object {}
        )
    }

    if(end !is Number) {
        throw PiktWrongArgumentTypeException(
                parameterName = "end",
                argumentValue = end,
                expectedType = NUMBER,
                reference = object {}
        )
    }

    val step = if(end >= start || forceUp.bool) 1 else -1
    return IntProgression.fromClosedRange(start.toInt(), end.toInt(), step).toMutableList()
}