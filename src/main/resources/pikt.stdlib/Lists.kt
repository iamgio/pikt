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
 * Generates an ascending range of integers from [start] to [end].
 * @param start start number
 * @param end end number
 */
fun range(start: Any, end: Any): MutableList<Int> {
    if(start !is Number && end !is Number) {
        throw RuntimeException("range(start, end) called with non-numeric values.")
    }
    return ((start as Number).toInt()..(end as Number).toInt()).toMutableList()
}