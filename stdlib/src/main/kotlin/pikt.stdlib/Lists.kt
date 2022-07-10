package pikt.stdlib

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
fun <T> listOf(vararg items: T): MutableList<T> {
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
        throw RuntimeException("Index (from listGetAt(list, index)) is not a number.")
    }
    if(index < 0 || index >= listSize(list)) {
        throw RuntimeException("Invalid index (from listGetAt(list, index)): index = $index, size = ${listSize(list)}")
    }
    val i = index.toInt()
    return when(list) {
        is List<*> -> list[i]
        is CharSequence -> list[i]
        else -> throw RuntimeException("List (from listGetAt(list, index)) is not a list or string.")
    }!!
}

/**
 * Adds values to a [list].
 * @param list list to be affected
 * @param items items to be added
 */
fun <T> addToList(list: MutableList<T>, vararg items: T) {
    list.addAll(items)
}

/**
 * Removes values from a [list].
 * @param list list to be affected
 * @param items items to be removed
 */
fun <T> removeFromList(list: MutableList<T>, vararg items: T) {
    list.removeAll(items)
}

/**
 * Generates a range of integers from [start] to [end].
 * @param start start number
 * @param end end number
 * @param forceUp whether the range should be ascending, even if [start] > [end]
 * @return list of integers from [start] to [end]
 */
@JvmOverloads
fun range(start: Any, end: Any, forceUp: Boolean = false): MutableList<Int> {
    if(start is Number && end is Number) {
        val step = if(end >= start || forceUp.bool) 1 else -1
        return IntProgression.fromClosedRange(start.toInt(), end.toInt(), step).toMutableList()
    }
    throw RuntimeException("range(start, end) called with non-numeric values.")
}