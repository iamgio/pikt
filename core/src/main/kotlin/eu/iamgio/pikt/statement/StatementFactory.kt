package eu.iamgio.pikt.statement

/**
 * A factory for available [Statement] types for a target output language.
 */
interface StatementFactory {

    /**
     * @return the statement that defines, or sets if they already exist, variables
     */
    fun variableAssignmentStatement(): Statement

    /**
     * @return the statement that calls a function without catching the output value
     */
    fun functionCallStatement(): Statement

    /**
     * @return the statement that executes a code block only if a condition is verified
     */
    fun ifStatement(): Statement

    /**
     * @return the statement that executes a code block only if the condition of the previous [ifStatement] failed
     */
    fun elseStatement(): Statement

    /**
     * @return the statement that defines a struct
     */
    fun structStatement(): Statement

    /**
     * @return the statement that executes a code block for each element of a collection
     */
    fun forEachStatement(): Statement

    /**
     * @return the statement that executes a code block as long as a condition is verified
     */
    fun whileStatement(): Statement

    /**
     * @return the statement that returns a value from a function and finishes its execution.
     *         If chained with a size of 2, performs a `break` on a loop
     */
    fun returnStatement(): Statement

    /**
     * @return the statement that opens a code block
     */
    fun lambdaOpenStatement(): Statement

    /**
     * @return the statement that closes a code block
     */
    fun lambdaCloseStatement(): Statement

    /**
     * @return the statement that prints an object to the output stream
     */
    fun printStatement(): Statement
}