@file:Suppress("FunctionName")

package pikt.stdlib.targets.jvm

fun target_printError(message: Any) {
    System.err.println(message)
}