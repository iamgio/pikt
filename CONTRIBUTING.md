# Contributing to Pikt
Hi there, I hope you enjoy Pikt! In this file you'll find a few guidelines in case you want to help to improve this out-of-the-scheme programming language.

## Expanding the standard library
Follow these steps if you wish to implement new functions or variables:
1) Head to the [stdlib package](src/main/resources/pikt.stdlib)
   and choose a file relevant to what you want to bring support to. If none of the existing fits your addition feel free to create a new `.kt` file (within the main package) that matches Kotlin naming guidelines.
   
2) Implement your function or variable as a top-level member, remember to add documentation for newcomers and use a clear name that explains its behavior.
   In case it is a function, arguments must be of the type `Any` (the superclass every Kotlin object shares) and should be cast at runtime.
   If the argument type doesn't match the one needed by your function don't worry about throwing a `RuntimeException` with a descriptive message.  
   Also, remember that Pikt doesn't support `import`: in case you need to use a non-default package, use the syntax `val x = com.package.X()`.  
   Example from `Lists.kt`:
   
```kotlin
/**
 * Generates an ascending range of integers from [start] to [end].
 * @param start start number
 * @param end end number
 * @return list of integers from [start] to end
 */
fun range(start: Any, end: Any): MutableList<Int> {
    if(start is Number && end is Number) {
        return (start.toInt()..end.toInt()).toMutableList()
    }
    throw RuntimeException("range(start, end) called with non-numeric values.")
}
```

3) In case your function has platform-dependent features you will need to implement it for both JVM and native targets.
   Head to the [targets](src/main/resources/pikt.stdlib/targets) package and write your function, preceeded by `target_` in `JVM.kt` and `Native.kt`.  
   After that, call the platform-specific function within the global one.  
   Example:
   
```kotlin
// Print.kt

/**
 * Prints a message to the [System.err] stream.
 * @param message text content
 */
fun printError(message: Any) {
   target_printError(message)
}

// JVM.kt

fun target_printError(message: Any) {
   System.err.println(message)
}

// Native.kt

fun target_printError(message: Any) {
   val STDERR = platform.posix.fdopen(2, "w")
   platform.posix.fprintf(STDERR, message.toString() + "\n")
   platform.posix.fflush(STDERR)
}
```

4) Link a unique color to your function or variable from the [default scheme](src/main/resources/properties/colors.properties)
   by appending `stdlib.name=YOURHEX`, where `name` should match your function/variable name (case-sensitive) and `YOURHEX` is the hexadecimal color linked to it, without the `#` and uppercase.
   
5) Test your new feature.

6) Open a [pull request](https://github.com/iAmGio/pikt/pulls).