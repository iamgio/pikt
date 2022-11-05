package pikt.stdlib

import pikt.error.PiktNoSuchElementException
import kotlin.collections.get as mapGet // By default it is overridden by Pikt's Objects.get
import kotlin.collections.set as mapSet

/**
 * Defines a struct: a data structure with writable members (or fields) associated with any value.
 *
 * @param members struct members as `key=value` pairs
 * @author Giorgio Garofalo
 */
open class Struct(vararg members: Pair<String, Any>) {

    /**
     * Struct members as a read-and-write map.
     */
    private val properties: HashMap<String, Any> = hashMapOf(*members)

    /**
     * @return the value associated with [property]'s `toString()`
     * @param property member of this struct associated with the desired value
     * @throws PiktNoSuchElementException if [property]'s `toString()` is not a member of this struct
     */
    operator fun get(property: Any) = properties.mapGet(property.toString())
        ?: throw PiktNoSuchElementException(property, reference = object {})

    /**
     * Sets the value associated with [property]'s `toString()`
     * @param property member of this struct associated with the desired value
     * @param value new value to overwrite
     * @throws PiktNoSuchElementException if [property]'s `toString()` is not a member of this struct
     */
    operator fun set(property: Any, value: Any) {
        val key = property.toString()
        if(properties.containsKey(key)) {
            properties.mapSet(key, value)
        } else {
            throw PiktNoSuchElementException(property, reference = object {})
        }
    }

    override fun toString(): String = properties.toString()
}