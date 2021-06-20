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
    constructor(pixel: Pixel) : this(pixel.codeContent)
}

/**
 * A member linked to a constant value.
 *
 * @param name Kotlin name of the constant
 */
data class ConstantMember(override val name: String) : ScopeMember {
    constructor(pixel: Pixel) : this(pixel.codeContent)
}

/**
 * A member linked to a method.
 *
 * @param name Kotlin name of the method
 * @param argumentsSize amount of arguments
 */
data class MethodMember(override val name: String, val argumentsSize: Int) : ScopeMember {
    constructor(pixel: Pixel, argumentsSize: Int) : this(pixel.codeContent, argumentsSize)
}