package eu.iamgio.pikt.explain.data

import java.io.File

/**
 * Defines a way to retrieve code from a given input.
 *
 * @author Giorgio Garofalo
 */
sealed interface CodeSource {

    /**
     * @param codeSource source to retrieve the code from
     * @return lines of code
     */
    fun getCodeLines(codeSource: String): List<String>
}

/**
 * A code retriever directly from text.
 */
object PlainTextCodeSource : CodeSource {
    override fun getCodeLines(codeSource: String): List<String> {
        return codeSource.replace("\\n", "\n").lines()
    }
}

/**
 * A code retriever from a text file.
 */
object FileCodeSource : CodeSource {
    override fun getCodeLines(codeSource: String): List<String> {
        return File(codeSource).readLines()
    }
}

/**
 * A retriever of no code.
 */
object EmptyCodeSource : CodeSource {
    override fun getCodeLines(codeSource: String): List<String> {
        return emptyList()
    }
}