package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.Pixel

/**
 * A scope that defines which members other statement can access.
 *
 * @param parent parent scope. `null` if this scope is the global one
 * @param ownMembers members defined from this scope
 * @author Giorgio Garofalo
 */
data class Scope(private val parent: Scope?, private val ownMembers: MutableMap<String, ScopeMember> = hashMapOf()) {

    private fun getAllMembers(map: MutableMap<String, ScopeMember> = hashMapOf()): MutableMap<String, ScopeMember> {
        map.putAll(ownMembers)
        if(parent != null) map.putAll(parent.getAllMembers(map))
        return map
    }

    /**
     * Members available within this scope.
     */
    private val allMembers: MutableMap<String, ScopeMember>
        get() = getAllMembers()

    /**
     * The amount of scopes that wrap either this one or its [parent].
     */
    val level: Int
        get() = parent?.level?.plus(1) ?: 0

    /**
     * Pushes a member to this scope.
     * @param pixel pixel to register as a member
     * @param member scope member
     */
    fun push(pixel: Pixel, member: ScopeMember) {
        ownMembers[pixel.id] = member
    }

    /**
     * @param pixel pixel linked to a member
     * @return member linked to [pixel] if it were registered within this scope, `null` otherwise
     */
    operator fun get(pixel: Pixel): ScopeMember? = allMembers[pixel.id]

    /**
     * @param pixel pixel to check existance for
     * @return whether [pixel] was registered within this scope
     */
    operator fun contains(pixel: Pixel): Boolean = get(pixel) != null
}
