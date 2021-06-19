package eu.iamgio.pikt.statement

import eu.iamgio.pikt.image.Pixel

/**
 * A scope that defines which members other statement can access.
 *
 * @param parent parent scope. `null` if this scope is the global one
 * @param ownMembers members defined from this scope
 * @author Giorgio Garofalo
 */
data class Scope(private val parent: Scope?, private val ownMembers: MutableSet<ScopeMember> = mutableSetOf()) {

    /**
     * Members available within this scope.
     */
    private val allMembers: Set<ScopeMember>
        get() = ownMembers + (parent?.allMembers ?: emptyList())

    /**
     * Pushes a member to this scope.
     * @param pixel pixel to register as a member
     * @param type member type
     */
    fun push(pixel: Pixel, type: ScopeMember.Type) {
        ownMembers += ScopeMember(pixel.id, type)
    }

    /**
     * @param pixel pixel linked to a member
     * @return member linked to [pixel] if it were registered within this scope, `null` otherwise
     */
    operator fun get(pixel: Pixel): ScopeMember? = allMembers.firstOrNull { it.id == pixel.id }

    /**
     * @param pixel pixel to check existance for
     * @return whether [pixel] was registered within this scope
     */
    operator fun contains(pixel: Pixel): Boolean = get(pixel) != null
}

/**
 * A declared member within a [Scope].
 *
 * @param id member ID from its pixel
 * @param type member type
 */
data class ScopeMember(val id: String, val type: Type) {
    enum class Type {
        VARIABLE,
        CONSTANT, // TODO implement constants
        METHOD
    }
}

