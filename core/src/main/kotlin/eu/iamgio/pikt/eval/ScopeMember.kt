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
    fun isApplicableFor(argumentsSize: Int) = overloads.any { it.isApplicableFor(argumentsSize) || it.hasVarArgs }

    /**
     * An overload of this function.
     *
     * @param argumentsSize amount of arguments
     */
    data class Overload(val argumentsSize: Int) {
        val hasVarArgs: Boolean
            get() = argumentsSize == VARARG_ARGUMENTS_AMOUNT

        /**
         * @return whether the amount of arguments of this overload matches [argumentsSize]
         */
        fun isApplicableFor(argumentsSize: Int) = this.argumentsSize == argumentsSize

        companion object {
            /**
             * The standard amount of arguments if an [Overload] includes varargs.
             */
            const val VARARG_ARGUMENTS_AMOUNT = -1
        }
    }
}