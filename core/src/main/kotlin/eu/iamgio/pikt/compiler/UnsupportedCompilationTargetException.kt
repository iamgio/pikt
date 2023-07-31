package eu.iamgio.pikt.compiler

/**
 * Exception thrown when a language that Pikt can transpile code to does not support a given compilation target.
 *
 * @param commandGenerator generator of command-line commands to communicate with the compiler tools for the language
 * @param target the unsupported compilation target
 */
class UnsupportedCompilationTargetException(
    commandGenerator: CompilerCommandGenerator,
    target: CompilationTarget
) : UnsupportedOperationException(
    "Target $target is unsupported by the selected language (delegated by ${commandGenerator.javaClass.simpleName})"
)
