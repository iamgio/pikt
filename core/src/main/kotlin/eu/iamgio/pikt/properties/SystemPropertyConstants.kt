package eu.iamgio.pikt.properties

/**
 * Supported system properties (defined via `-Dproperty[=value]`) that affect several behaviors.
 *
 * @author Giorgio Garofalo
 */
object SystemPropertyConstants {

    // Base properties

    /**
     * Source image.
     */
    const val SOURCE = "source"

    /**
     * Output executables.
     */
    const val OUTPUT = "out"

    /**
     * Compilation targets.
     */
    const val TARGETS = "targets"

    /**
     * External libraries.
     */
    const val LIBRARIES = "lib"

    /**
     * Color scheme file.
     */
    const val COLOR_SCHEME = "colors"

    /**
     * Kotlin/JVM compiler.
     */
    const val JVM_COMPILER = "jvmcompiler"

    /**
     * Kotlin/Native compiler.
     */
    const val NATIVE_COMPILER = "nativecompiler"

    // Project info

    /**
     * Project info file.
     * @see eu.iamgio.pikt.project.PiktProjectInfo
     */
    const val PROJECT_INFO = "project"

    /**
     * Project info task name.
     */
    const val PROJECT_INFO_TASK = "task"

    // Other properties

    /**
     * If this system property is enabled, [eu.iamgio.pikt.image.PixelReader] throws an exception on error.
     */
    const val THROW_EXCEPTION_ON_READER_ERROR = "readerexc"

    /**
     * If this system property is enabled, [eu.iamgio.pikt.exit.exit] throws an exception instead of exiting.
     */
    const val NO_EXIT = "noexit"

    // Also present: 'loglevel' to set the log level (defined in the Log4J configuration).
}