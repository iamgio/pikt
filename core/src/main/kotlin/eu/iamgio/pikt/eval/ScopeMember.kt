package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.Pixel

/**
 * A declared member within a [Scope].
 *
 * @author Giorgio Garofalo
 */
sealed interface ScopeMember {
    val name: String
}

/**
 * A member linked to a variable value.
 *
 * @param name Kotlin name of the variable
 */
data class VariableMember(override val name: String) : ScopeMember {
    constructor(pixel: Pixel) : this(pixel.id)
}

/**
 * A member linked to a constant value.
 *
 * @param name Kotlin name of the constant
 */
data class ConstantMember(override val name: String) : ScopeMember {
    constructor(pixel: Pixel) : this(pixel.id)
}

/**
 * A member linked to a struct.
 *
 * @param name Kotlin name of the struct
 * @param overload arguments information
 */
data class StructMember(override val name: String, val overload: FunctionMember.Overload) : ScopeMember {
    constructor(pixel: Pixel, overload: FunctionMember.Overload) : this(pixel.id, overload)
}

/**
 * A member linked to a function.
 *
 * @param name Kotlin name of the function
 * @param overloads function overloads
 */
data class FunctionMember(override val name: String, val overloads: MutableList<Overload>, val isLibraryFunction: Boolean = false) : ScopeMember {

    constructor(pixel: Pixel, overload: Overload) : this(pixel.id, mutableListOf(overload))

    /**
     * @param argumentsSize amount of arguments from method call
     * @return whether the passed amount of arguments matches the definition
     */
    fun isApplicableFor(argumentsSize: Int) = overloads.any { it.isApplicableFor(argumentsSize) }

    /**
     * A single parameter taken by a function.
     *
     * @param name name of the parameter, `null` if undefined
     * @param isVarArg whether this parameter accepts multiple arguments
     */
    data class Parameter(val name: String?, val isVarArg: Boolean = false)

    /**
     * An overload of a function.
     *
     * @param parameters parameters accepted by this overload
     */
    data class Overload(val parameters: List<Parameter>) {

        private val hasVarArgs: Boolean
            get() = parameters.any { it.isVarArg }

        /**
         * @return whether the amount of arguments of this overload matches [argumentsAmount]
         */
        fun isApplicableFor(argumentsAmount: Int) = this.parameters.size == argumentsAmount || this.hasVarArgs
    }
}