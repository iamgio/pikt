package eu.iamgio.pikt.logger

import eu.iamgio.pikt.log.pixel.PixelLogger
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Bridge for logging utilities.
 *
 * @author Giorgio Garofalo
 */
object Log {

    /**
     * The standard text logger.
     */
    val logger: Logger by lazy { LogManager.getLogger(this.javaClass.name) }

    /**
     * The pixel logger to be used.
     */
    var pixelLogger: PixelLogger? = null
}