package pikt.stdlib

/**
 * Enables generic number + number operation.
 * @param other number to be added
 * @return [this] + [other]
 */
operator fun Number.plus(other: Number): Number {
    return when (this) {
        is Long   -> this.toLong()   + other.toLong()
        is Int    -> this.toInt()    + other.toInt()
        is Short  -> this.toShort()  + other.toShort()
        is Byte   -> this.toByte()   + other.toByte()
        is Double -> this.toDouble() + other.toDouble()
        is Float  -> this.toFloat()  + other.toFloat()
        else      -> throw RuntimeException("Plus operation (+) called with at least one non-numeric value: $this + $other")
    }
}

/**
 * Enables generic number - number operation.
 * @param other number to be subtracted
 * @return [this] - [other]
 */
operator fun Number.minus(other: Number): Number {
    return when (this) {
        is Long   -> this.toLong()   - other.toLong()
        is Int    -> this.toInt()    - other.toInt()
        is Short  -> this.toShort()  - other.toShort()
        is Byte   -> this.toByte()   - other.toByte()
        is Double -> this.toDouble() - other.toDouble()
        is Float  -> this.toFloat()  - other.toFloat()
        else      -> throw RuntimeException("Minus operation (-) called with at least one non-numeric value: $this - $other")
    }
}

/**
 * Enables generic number * number operation.
 * @param other number to be multiplied by
 * @return [this] * [other]
 */
operator fun Number.times(other: Number): Number {
    return when (this) {
        is Long   -> this.toLong()   * other.toLong()
        is Int    -> this.toInt()    * other.toInt()
        is Short  -> this.toShort()  * other.toShort()
        is Byte   -> this.toByte()   * other.toByte()
        is Double -> this.toDouble() * other.toDouble()
        is Float  -> this.toFloat()  * other.toFloat()
        else      -> throw RuntimeException("Times operation (*) called with at least one non-numeric value: $this * $other")
    }
}

/**
 * Enables generic number / number operation.
 * @param other number to be divided by
 * @return [this] / [other]
 */
operator fun Number.div(other: Number): Number {
    return when (this) {
        is Long   -> this.toLong()   / other.toLong()
        is Int    -> this.toInt()    / other.toInt()
        is Short  -> this.toShort()  / other.toShort()
        is Byte   -> this.toByte()   / other.toByte()
        is Double -> this.toDouble() / other.toDouble()
        is Float  -> this.toFloat()  / other.toFloat()
        else      -> throw RuntimeException("Divide operation (/) called with at least one non-numeric value: $this / $other")
    }
}

/**
 * Enables generic number % number operation.
 * @param other number to evaluate the modulo on
 * @return [this] % [other]
 */
operator fun Number.rem(other: Number): Number {
    return when (this) {
        is Long   -> this.toLong()   % other.toLong()
        is Int    -> this.toInt()    % other.toInt()
        is Short  -> this.toShort()  % other.toShort()
        is Byte   -> this.toByte()   % other.toByte()
        is Double -> this.toDouble() % other.toDouble()
        is Float  -> this.toFloat()  % other.toFloat()
        else      -> throw RuntimeException("Rem operation (%) called with at least one non-numeric value: $this % $other")
    }
}