package pikt.stdlib

import pikt.error.PiktInvalidOperationException

/**
 * Sometimes the output code may contain something like this:
 * var x = ...
 * var y = x()
 *
 * This function ignores the redundant empty call.
 */
operator fun <T> T.invoke(): T = this

/**
 * Generic obj + obj operation.
 */
operator fun Any.plus(other: Any): Any {
    return when {
        this is Number && other is Number -> {
            this + other
        }
        this is String || other is String -> {
            this.toString() + other.toString()
        }
        else -> throw PiktInvalidOperationException(
                this, "+", other,
                reference = object {}
        )
    }
}

/**
 * Generic obj - obj operation.
 */
operator fun Any.minus(other: Any): Any {
    return when {
        this is Number && other is Number -> {
            this - other
        }
        else -> throw PiktInvalidOperationException(
                this, "-", other,
                reference = object {}
        )
    }
}

/**
 * Generic obj * obj operation.
 */
operator fun Any.times(other: Any): Any {
    return when {
        this is Number && other is Number -> {
            this * other
        }
        this is String && other is Number -> {
            this.repeat(other.toInt())
        }
        this is Number && other is String -> {
            other.repeat(this.toInt())
        }
        else -> throw PiktInvalidOperationException(
                this, "*", other,
                reference = object {}
        )
    }
}

/**
 * Generic obj / obj operation.
 */
operator fun Any.div(other: Any): Any {
    return when {
        this is Number && other is Number -> {
            this / other
        }
        else -> throw PiktInvalidOperationException(
                this, "/", other,
                reference = object {}
        )
    }
}

/**
 * Generic obj % obj operation.
 */
operator fun Any.rem(other: Any): Any {
    return when {
        this is Number && other is Number -> {
            this % other
        }
        else -> throw PiktInvalidOperationException(
                this, "%", other,
                reference = object {}
        )
    }
}

/**
 * Generic `[...]` operation.
 *
 * Gets a value from a list or string. It can be accessed from Pikt via the dot operator.
 * @param index numeric index of the value within the list, from 0 (inclusive) to the size of the list (exclusive)
 */
operator fun Any.get(index: Any): Any {
    return listGetAt(this, index)
}

/**
 * Generic `[...] = ...` operation.
 *
 * Sets a value in a list. It can be accessed from Pikt via the set variable + dot operators.
 * @param index numeric index of the value within the list, from 0 (inclusive) to the size of the list (exclusive)
 */
operator fun Any.set(index: Any, value: Any): Any {
    return listSetAt(this, index, value)
}

/**
 * Generic `<`, `<=`, `>`, `>=` operations.
 */
operator fun Any.compareTo(other: Any): Int {
    @Suppress("UNCHECKED_CAST")
    return (this as? Comparable<Any>)?.compareTo(other) ?: 0
}

/**
 * Gets a boolean value from any nullable object value.
 * This is automatically inserted in `if` blocks.
 * @return `true` if either this object is not null or this is called on a boolean whose value is `true`, `false` otherwise.
 */
val Any?.bool: Boolean
    get() {
        if(this is Boolean) return this
        return this != null
    }