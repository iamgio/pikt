package eu.iamgio.pikt.tests

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue


class PiktTest {

    private val launcher = PiktTestLauncher()
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
    fun `a tree that prints 'A tree!'`() {
        with(launch("tree", scheme = "tree")) {
            assertTrue {
                first() == "A tree!"
            }
        }
    }
}