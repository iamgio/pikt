package pikt.stdlib

/**
 * Sometimes the output code may contain something like this:
 * var x = ...
 * var y = x()
 *
 * This function ignores the redundant empty call.
 */
operator fun <T> T.invoke(): T = this