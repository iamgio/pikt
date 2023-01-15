package eu.iamgio.pikt.tests

import org.junit.jupiter.api.Test
import java.io.InputStream
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails

private class InternalTestLauncher : PiktTestLauncher() {
    override fun getImage(name: String): InputStream {
        return PiktTest::class.java.getResourceAsStream("/$name.png")!!
    }

    override fun getColorScheme(colorSchemeName: String): InputStream {
        return PiktTest::class.java.getResourceAsStream("/schemes/$colorSchemeName.properties")!!
    }
}

class PiktTest {

    private val launcher = InternalTestLauncher()
    private fun launch(name: String, scheme: String? = null) = launcher.launch(name, scheme)

    @Test
    fun `hello world`() {
        with(launch("hello_world")) {
            assertEquals("Hello world!", first())
        }
    }

    @Test
    fun `sum of 3 integers`() {
        with(launch("int_sum")) {
            assertContains(this, "51")
        }
    }

    @Test
    fun `check if string is not an integer`() {
        with(launch("not_int")) {
            assertEquals("OK", first())
        }
    }

    @Test
    fun `print each element of an int list`() {
        with(launch("list_foreach")) {
            assertContentEquals(listOf("13", "32", "60"), this)
        }
    }

    @Test
    fun `sum two struct fields`() {
        // This test creates a struct with 3 parameters.
        // The first one is set to 1, the second one is set to 2.
        // The third one is the sum of the other two.
        with(launch("structs_sum")) {
            assertContentEquals(listOf("1", "2", "3"), this)
        }
    }

    @Test
    fun `struct typing`() {
        // This test creates a struct and accesses its members inside a function
        // where an instance is passed as an argument.
        // See: https://github.com/iamgio/pikt/issues/4
        with(launch("struct_typing")) {
            assertEquals("2", first())
        }
    }

    @Test
    fun `nesting awareness check`() {
        // This test mixes the two types of nesting and lets Pikt decide which one to use for each case:
        // First a struct "dot-type" nesting, then a list "brackets-type" nesting.
        with(launch("nesting")) {
            assertEquals("0", first())
        }
    }

    @Test
    fun `print numbers from 0 to 10`() {
        with(launch("range_foreach")) {
            assertContentEquals((0..10).map { it.toString() }, this)
        }
    }

    @Test
    fun `fibonacci n=20`() {
        with(launch("fibonacci", scheme = "fibonacci")) {
            assertEquals("4181", last())
        }
    }

    @Test
    fun `fizzbuzz n=30`() {
        with(launch("fizzbuzz", scheme = "fizzbuzz")) {
            assertEquals(31, size)
            assertEquals("FizzBuzz", first())
            assertEquals("1", this[1])
            assertEquals("2", this[2])
            assertEquals("Fizz", this[3])
            assertEquals("4", this[4])
            assertEquals("Buzz", this[5])
            assertEquals("FizzBuzz", last())
        }
    }

    @Test
    fun `print prime numbers from 0 to 30`() {
        with(launch("prime_numbers", scheme = "prime_numbers")) {
            assertEquals(10, size)
            assertEquals("2", first())
            assertEquals("29", last())
        }
    }

    @Test
    fun `san francisco insertion sort`() {
        val values = listOf(7, 10, 1, 46, 19, 8)
        with(launch("insertion_sort", scheme = "insertion_sort")) {
            assertEquals(2, size)
            assertEquals(values.toString(), component1())
            assertEquals(values.sorted().toString(), component2())
        }
    }

    @Test
    fun `a tree that prints 'A tree!'`() {
        with(launch("tree", scheme = "tree")) {
            assertEquals("A tree!", first())
        }
    }

    @Test
    fun `reverse string`() {
        with(launch("reverse", scheme = "reverse")) {
            assertEquals("Reversed".reversed(), first())
        }
    }

    @Test
    fun `error, unresolved reference`() {
        assertFails { launch("error_unresolved") }
    }

    @Test
    fun `error, foreach with no body`() {
        assertFails { launch("error_foreach_no_body") }
    }

    @Test
    fun `error, variable not initialized`() {
        assertFails { launch("error_variable_not_initialized") }
    }
}