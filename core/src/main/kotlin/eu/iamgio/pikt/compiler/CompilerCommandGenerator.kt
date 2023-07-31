package eu.iamgio.pikt.compiler

import java.io.File

/**
 * A generator that provides executable commands to run compilation and interpretation tasks,
 * possibly by invoking the compiler of the target transpilation language, which is supposed
 * to be installed on the machine.
 * A compilation happens against one target platform at a time.
 * If a given platform target is unsupported by the language target,
 * an [UnsupportedCompilationTargetException] is thrown.
 */
interface CompilerCommandGenerator {

    /**
     * Generates the command needed to compile a source into an [outputFile] for a specific [target] platform.
     * @param target target platform
     * @param outputFile file to save the result of the compilation to
     * @return an array containing the program to launch and its arguments
     */
    fun generateCompilationCommand(target: CompilationTarget, outputFile: File): Array<String>

    /**
     * Generates the command needed to interpret a source on the fly on a specific [target] platform.
     * @param target target platform
     * @return an array containing the program to launch and its arguments
     */
    fun generateInterpretationCommand(target: CompilationTarget): Array<String>
}
