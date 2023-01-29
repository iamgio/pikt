package eu.iamgio.pikt.command.help

import java.io.InputStream

/**
 * A general parser for data that contains information about command usage and more.
 *
 * @see HelpData
 * @author Giorgio Garofalo
 */
interface HelpDataParser {

    /**
     * Parses the content of an input stream.
     * @param inputStream raw data to parse
     * @return parsed data
     */
    fun parse(inputStream: InputStream): HelpData
}