package eu.iamgio.pikt.statement

/**
 * A factory for available [Statement] types for a target output language.
 */
interface StatementFactory {

    /**
     * @return the statement that defines, or sets if they already exist, variables
     */
    fun variableAssignment(): Statement

    /**
     * @return the statement that calls a function without catching the output value
     */
    fun functionCall(): Statement

    /**
     * @return the statement that executes a code block only if a condition is verified
     */
    fun `if`(): Statement

    /**
     * @return the statement that executes a code block only if the condition of the previous [if] statement failed
     */
    fun `else`(): Statement

    /**
     * @return the statement that defines a struct
     */
    fun struct(): Statement

    /**
     * @return the statement that executes a code block for each element of a collection
     */
    fun forEach(): Statement

    /**
     * @return the statement that executes a code block as long as a condition is verified
     */
    fun `while`(): Statement

    /**
     * @return the statement that returns a value from a function and finishes its execution.
     *         If chained with a size of 2, performs a `break` on a loop
     */
    fun `return`(): Statement

    /**
     * @return the statement that opens a code block
     */
    fun lambdaOpen(): Statement

    /**
     * @return the statement that closes a code block
     */
    fun lambdaClose(): Statement

    /**
     * @return the statement that prints an object to the output stream
     */
    fun print(): Statement
}