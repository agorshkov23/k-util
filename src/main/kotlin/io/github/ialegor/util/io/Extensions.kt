package io.github.ialegor.util.io

import java.io.File
import java.io.OutputStream
import java.time.LocalDateTime

fun File.makeParentDirs(): File {
    this.parentFile.mkdirs()
    return this
}

fun File.child(name: String): File {
    return File(this, name)
}

fun writeFile(file: File, writer: (OutputStream) -> Unit) =
    FileWriter().write(file, writer)

fun writeFile(file: File, timestamp: LocalDateTime, writer: (OutputStream) -> Unit) =
    HistoricalFileWriter(timestamp).write(file, writer)
