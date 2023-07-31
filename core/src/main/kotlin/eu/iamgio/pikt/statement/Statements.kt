package eu.iamgio.pikt.statement

import eu.iamgio.pikt.image.Pixel

/**
 * Single-instance class that contains statements information.
 *
 * @author Giorgio Garofalo
 */
object Statements {

    private val statements = mutableListOf<Statement>()

    /**
     * Registers a new statement.
     * @param statement statement to be registered
     */
    fun register(statement: Statement) {
        statements += statement
    }

    /**
     * Gets a statement matching the color of [pixel] based on color scheme.
     * @return statement by pixel color if exists. `null` otherwise
     */
    fun byPixel(pixel: Pixel): Statement? = statements.firstOrNull { it.matches(pixel) }
}
