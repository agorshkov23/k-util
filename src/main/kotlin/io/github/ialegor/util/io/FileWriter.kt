package io.github.ialegor.util.io

import io.github.ialegor.util.logging.log
import java.io.File
import java.io.OutputStream

open class FileWriter {

    protected val log = log()

    open fun write(file: File, writer: (OutputStream) -> Unit) {
        file.makeParentDirs()
        log.debug { "Writing file ${file.absoluteFile}" }
        file.outputStream().use(writer)
    }
}
