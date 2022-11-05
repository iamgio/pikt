package pikt.error

/**
 * An exception thrown when an element does not exist within a data structure.
 *
 * @param element the missing element
 * @param reference a reference object used to retrieve the caller function, usually defined as `object {}`
 * @author Giorgio Garofalo
 */
class PiktNoSuchElementException(element: Any, reference: Any) : PiktException(
    "$element is not an element.",
    reference
)