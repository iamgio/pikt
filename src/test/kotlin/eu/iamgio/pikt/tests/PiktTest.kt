package eu.iamgio.pikt.tests

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue


class PiktTest {

    private val launcher = PiktTestLauncher()
    private fun launch(name: String) = launcher.launch(name)

    @Test
    fun `sum of 3 integers`() {
        with(launch("int_sum")) {
            assertTrue {
                contains("51")
            }
        }
    }
}