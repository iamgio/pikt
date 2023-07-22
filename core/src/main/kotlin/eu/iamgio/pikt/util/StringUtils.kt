package eu.iamgio.pikt.util

/**
 * Example:
 * - `hello` turns into `"hello"`
 * - `1 "2" 3` turns into `"1 \"2\" 3"`
 * @return [this] string into a code-friendly string value
 */
fun String.stringify() = "\"" + replace("\\", "\\\\") + "\""