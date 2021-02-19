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
fun range(start: Int, end: Int): MutableList<Int> {
    return (start..end).toMutableList()
}