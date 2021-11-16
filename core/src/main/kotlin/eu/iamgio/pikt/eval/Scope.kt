package eu.iamgio.pikt.eval

import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.lib.JarLibrary
import eu.iamgio.pikt.lib.Libraries

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
     * Pushes a member, linked by a [Pixel], to this scope.
     * @param pixel pixel to register as a member
     * @param member scope member
     */
    fun push(pixel: Pixel, member: ScopeMember) {
        ownMembers[pixel.id] = member
    }

    /**
     * Pushes a member, linked by a hexadecimal color, to this scope.
     * @param hex color to register as a member
     * @param member scope member
     */
    fun push(hex: String, member: ScopeMember) {
        ownMembers[hex] = member
    }

    /**
     * @param pixel pixel linked to a member
     * @return member linked to [pixel] if it were registered within this scope, `null` otherwise
     */
    operator fun get(pixel: Pixel): ScopeMember? = allMembers[pixel.id]

    /**
     * @param hex hexadecimal color linked to a member
     * @return member linked to [hex] if it were registered within this scope, `null` otherwise
     */
    operator fun get(hex: String): ScopeMember? = allMembers[hex]

    /**
     * @param pixel pixel to check existance for
     * @return whether [pixel] was registered within this scope
     */
    operator fun contains(pixel: Pixel): Boolean = get(pixel) != null

    companion object {
        /**
         * Builds the main scope filled with library functions.
         * @param libraries supplied libraries
         * @return the main scope of the program
         */
        fun buildMainScope(libraries: List<JarLibrary>): Scope {
            val scope = Scope(parent = null)
            libraries.forEach { library ->
                // We look up available classes and functions from libraries.
                val helper = library.reflectionHelper()
                helper.getAllMethods().forEach { method ->
                    // The colors of the function are retrieved.
                    Libraries.getColorsFor(method.name)?.colors?.forEach { hex ->
                        // The linked function member is created or updated.
                        val function = scope[hex] as? FunctionMember ?: FunctionMember(method.name, mutableListOf(), isLibraryFunction = true)
                        function.overloads += helper.createFunctionOverload(method)
                        // The function is pushed to the scope.
                        scope.push(hex, function)
                    }
                }
            }
            return scope
        }
    }
}
