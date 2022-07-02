package pikt.stdlib

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
 * @return size (length) of the [list]
 */
fun size(list: Any): Int {
    return if(list is List<*>) list.size else -1
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