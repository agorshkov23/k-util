package io.github.ialegor.util.io

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime

internal class ExtensionsTest {

    private val dir = File("target/tests/FileWriter")

    @BeforeEach
    fun beforeEach() {
        if (dir.exists()) {
            dir.deleteRecursively()
        }
    }

    @Test
    fun test_writeFile() {
        val file = File(dir, "unique.txt")
        assertFalse(file.exists())
        writeFile(file) { os ->
            os.writer(utf8).use { osw ->
                osw.write("unique file")
            }
        }
        assertTrue(file.exists())
        assertTrue(file.length() > 0)
    }

    @Test
    fun test_writeHistoricalFile() {
        val timestamp = LocalDateTime.parse("2020-01-02T03:04:05.678")

        val file = File(dir, "historical.txt")
        val historicalFile = File(dir, "history").child("historical.20200102_030405.txt")

        assertFalse(file.exists())
        assertFalse(historicalFile.exists())
        writeFile(file, timestamp) { os ->
            os.writer(utf8).use { osw ->
                osw.write("unique file")
            }
        }

        assertTrue(file.exists())
        assertTrue(file.length() > 0)
        assertTrue(historicalFile.exists())
        assertTrue(historicalFile.length() > 0)
    }
}
