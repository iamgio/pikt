package eu.iamgio.pikt.tests

import org.junit.jupiter.api.Test
import java.io.InputStream
import kotlin.test.assertFails
import kotlin.test.assertTrue

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
            assertTrue {
                first() == "Hello world!"
            }
        }
    }

    @Test
    fun `sum of 3 integers`() {
        with(launch("int_sum")) {
            assertTrue {
                contains("51")
            }
        }
    }

    @Test
    fun `check if string is not an integer`() {
        with(launch("not_int")) {
            assertTrue {
                first() == "OK"
            }
        }
    }

    @Test
    fun `print each element of an int list`() {
        with(launch("list_foreach")) {
            assertTrue {
                equals(listOf("13", "32", "60"))
            }
        }
    }

    @Test
    fun `sum two struct fields`() {
        // This test creates a struct with 3 parameters.
        // The first one is set to 1, the second one is set to 2.
        // The third one is the sum of the other two.
        with(launch("structs_sum")) {
            assertTrue {
                equals(listOf("1", "2", "3"))
            }
        }
    }

    @Test
    fun `struct typing`() {
        // This test creates a struct and accesses its members inside a function
        // where an instance is passed as an argument.
        // See: https://github.com/iamgio/pikt/issues/4
        with(launch("struct_typing")) {
            assertTrue {
                first() == "2"
            }
        }
    }

    @Test
    fun `nesting awareness check`() {
        // This test mixes the two types of nesting and lets Pikt decide which one to use for each case:
        // First a struct "dot-type" nesting, then a list "brackets-type" nesting.
        with(launch("nesting")) {
            assertTrue {
                first() == "0"
            }
        }
    }

    @Test
    fun `print numbers from 0 to 10`() {
        with(launch("range_foreach")) {
            assertTrue {
                equals((0..10).map { it.toString() })
            }
        }
    }

    @Test
    fun `fibonacci n=20`() {
        with(launch("fibonacci", scheme = "fibonacci")) {
            assertTrue {
                last() == "4181"
            }
        }
    }

    @Test
    fun `print prime numbers from 0 to 30`() {
        with(launch("prime_numbers", scheme = "prime_numbers")) {
            assertTrue {
                size == 10 && first() == "2" && last() == "29"
            }
        }
    }

    @Test
    fun `san francisco insertion sort`() {
        val values = listOf(7, 10, 1, 46, 19, 8)
        with(launch("insertion_sort", scheme = "insertion_sort")) {
            assertTrue { size == 2 }
            assertTrue { component1() == values.toString() }
            assertTrue { component2() == values.sorted().toString() }
        }
    }

    @Test
    fun `a tree that prints 'A tree!'`() {
        with(launch("tree", scheme = "tree")) {
            assertTrue {
                first() == "A tree!"
            }
        }
    }

    @Test
    fun `reverse string`() {
        with(launch("reverse", scheme = "reverse")) {
            assertTrue {
                first() == "Reversed".reversed()
            }
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