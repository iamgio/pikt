package eu.iamgio.pikt.compiler

// These contents from the compiler are skipped and not printed out.
const val KOTLIN_COMPILER_WARNING = "WARNING"
const val KOTLIN_COMPILER_XVERIFY = "-Xverify"

// This is printed by the default implementation of Interpreter and Compiler
// when an error is thrown by the Kotlin compiler.
const val KOTLIN_COMPILER_ERROR_MESSAGE_HEADER = """
―――――――――――――――――――――――――――――――――――――――――
An error was thrown by the Kotlin compiler.
If you believe this is a code generation error, please open an issue.
―――――――――――――――――――――――――――――――――――――――――
"""