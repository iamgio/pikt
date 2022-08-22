package pikt.error

import pikt.stdlib.NON_LIST_SIZE
import pikt.stdlib.listSize

/**
 * @param index index of the element that was accessed
 * @param size size of the list
 * @param reference a reference object used to retrieve the caller function, usually defined as `object {}`
 * @author Giorgio Garofalo
 */
class PiktIndexOutOfBoundsException(index: Number, size: Int, reference: Any) : PiktException(
        "Invalid index: index = $index (list size = $size)",
        reference
)

/**
 * Checks whether [index] exceeds the size of [list].
 * If it does, throws [PiktIndexOutOfBoundsException].
 */
fun checkIndexValidity(list: Any, index: Any, reference: Any) {
    if(index !is Number) {
        throw PiktWrongArgumentTypeException(
                parameterName = "index",
                argumentValue = index,
                expectedType = ValueType.NUMBER,
                reference = reference
        )
    }

    val size = listSize(list)
    val i = index.toInt()
    if(size != NON_LIST_SIZE && (i < 0 || i >= size)) {
        throw PiktIndexOutOfBoundsException(index, size, reference)
    }
}