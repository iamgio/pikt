package eu.iamgio.pikt.tests

import eu.iamgio.pikt.command.CliCommandsUtils
import eu.iamgio.pikt.command.CliCommandsUtils.merge
import eu.iamgio.pikt.command.CliCommandsUtils.parsed
import eu.iamgio.pikt.project.PiktProjectInfo
import eu.iamgio.pikt.project.PiktProjectInfoParser
import eu.iamgio.pikt.registerCommands
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CommandParsingTest {

    init {
        registerCommands()
    }

    private fun getProjectInfoFromResource(): PiktProjectInfo {
        val inputStream = javaClass.getResourceAsStream("/projectinfo/test-project-info.yml")!!
        return PiktProjectInfoParser(inputStream).parse()
    }

    @Test
    fun `command parsing`() {
        val commands = CliCommandsUtils.getRawCommands(arrayOf(
            "-printoutput", "-imgoutput=something", "-somethingelse"
        )).parsed()

        val expected = mapOf(
            "-printoutput" to null,
            "-imgoutput" to "something",
            null to null
        )

        assertContentEquals(expected.keys, commands.keys.map { it?.name })
        assertContentEquals(expected.values, commands.values)
    }

    @Test
    fun `project info parsing`() {
        val info = getProjectInfoFromResource()

        val expectedProperties = mapOf(
            "source" to "source.png",
            "colors" to "colors",
            "targets" to "jvm"
        )

        val expectedCommands = mapOf(
            "-printoutput" to null,
            "-interpret" to null,
            "-imgoutput" to "something_from_project_info"
        )

        assertEquals(expectedProperties, info.properties)
        assertEquals(expectedCommands, info.commands)

        val expectedTaskProperties = mapOf(
            "colors" to "newcolors",
        )

        val expectedTaskCommands = mapOf(
            "-imgoutput" to "something_from_task",
            "-recolorize" to null
        )

        val task = info.getTaskByName("mytask")

        assertNotNull(task)
        assertEquals(expectedTaskProperties, task.properties)
        assertEquals(expectedTaskProperties, task.properties)
    }

    @Test
    fun `project info merge`() {
        val info = getProjectInfoFromResource()

        System.setProperty("source", "something")
        info.applyProperties()

        assertEquals("something", System.getProperty("source")) // Overridden

        val commands = mapOf(
            "-nocompile" to null,
            "-imgoutput" to "something"
        )

        val expectedMergedCommands = mapOf(
            "-nocompile" to null,
            "-printoutput" to null,
            "-interpret" to null,
            "-imgoutput" to "something", // Overridden
        )

        assertEquals(expectedMergedCommands, commands.merge(info.commands))

        val expectedMergedCommandsWithTask = expectedMergedCommands + ("-recolorize" to null)
        val task = info.getTaskByName("mytask")

        assertNotNull(task)
        assertEquals(expectedMergedCommandsWithTask, commands.merge(info.commands).merge(task.commands))
    }
}