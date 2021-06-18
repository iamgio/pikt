package eu.iamgio.pikt.statement

/**
 * A scope that defines which members other statement can access.
 *
 * @param parent parent scope. `null` if this scope is the global one
 * @author Giorgio Garofalo
 */
data class Scope(private val parent: Scope?) {

    /**
     * Names of the variables from this scope.
     */
    val ownVariables = mutableSetOf<String>()

    /**
     * Names of the variables available within this scope.
     */
    val allVariables: Set<String>
        get() = ownVariables + (parent?.allVariables ?: emptyList())
}