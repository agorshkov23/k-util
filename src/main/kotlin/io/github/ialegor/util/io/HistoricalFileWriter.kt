package io.github.ialegor.util.io

import java.io.File
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoricalFileWriter(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val historyDirName: String = "history",
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"),
) : FileWriter() {

    override fun write(file: File, writer: (OutputStream) -> Unit) {
        val historyDir = File(file.parent, historyDirName)
        val historyFileName = listOf(file.nameWithoutExtension, timestamp.format(formatter), file.extension).joinToString(".")
        val historyFile = File(historyDir, historyFileName)

        super.write(historyFile, writer)

        log.debug { "Copying history file $historyFile to latest $file" }
        Files.copy(historyFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}
