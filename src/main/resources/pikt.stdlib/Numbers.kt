package pikt.stdlib

/**
 * Enables number + string operation.
 * @param string string to be appended
 * @return [string] appended to the string value of the number
 */
operator fun Number.plus(string: String) = this.toString() + string