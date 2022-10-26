# Contributing to Pikt
Hi there, I hope you enjoy Pikt! In this file you'll find a few guidelines in case you want to help to improve this out-of-the-scheme programming language.

## Expanding the standard library
Follow these steps if you wish to implement new functions or variables:
1) Head to [stdlib](stdlib/src/main/kotlin/pikt/stdlib)
   and choose a file relevant to what you want to bring support to. If none of the existing fits your addition feel free to create a new `.kt` file (within the main package) that matches Kotlin naming guidelines.
   
2) Implement your function or variable as a top-level member, remember to add documentation for newcomers and use a clear name that explains its behavior.
   In case it is a function, arguments must be of the type `Any` (the superclass all Kotlin objects share) and should be cast at runtime.  
   If the argument type doesn't match the one needed by your function don't worry about throwing a [`PiktException`](stdlib/src/main/kotlin/pikt/error/PiktException.kt) or subclass with a descriptive message.  

- Example from `Print.kt` that prints an object to the standard error stream:

```kotlin
/**
 * Prints a message to the [System.err] stream.
 * @param message text content
 */
fun printError(message: Any) {
    System.err.println(message)
}
```

- Example from `Lists.kt` that creates a list of numbers from `start` to `end`:
   
```kotlin
/**
 * Generates a range of integers from [start] to [end].
 * @param start start number
 * @param end end number
 * @param forceUp whether the range should be ascending, even if [start] > [end]
 * @return list of integers from [start] to [end]
 */
@JvmOverloads // Needed if using default parameters
fun range(start: Any, end: Any, forceUp: Any = false): MutableList<Int> {
   if(start !is Number) {
      throw PiktWrongArgumentTypeException(
         parameterName = "start",
         argumentValue = start,
         expectedType = NUMBER,
         reference = object {} // Needed to gain runtime information
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

   // Any.bool is defined in Objects.kt
   val step = if(end >= start || forceUp.bool) 1 else -1
   // This is a function from the Kotlin standard library
   return IntProgression.fromClosedRange(start.toInt(), end.toInt(), step).toMutableList()
}
```

3) Link a unique color to your function or variable from the [default scheme](core/src/main/resources/colors.properties)
   by appending `stdlib.name=YOURHEX`, where `name` should match your function/variable name (case-sensitive) and `YOURHEX` is the hexadecimal color linked to it, without the `#` and uppercase. The color can also be omitted, letting the users choose their own colors.

4) Run `mvn clean install -pl stdlib -am` and test your new feature.

5) Open a [pull request](https://github.com/iAmGio/pikt/pulls).