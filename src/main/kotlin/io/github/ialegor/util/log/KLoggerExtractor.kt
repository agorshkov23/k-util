package io.github.ialegor.util.log

import io.github.ialegor.util.time.format
import mu.KLogger
import java.time.Duration

class KLoggerExtractor<T>(
    private val kLogger: KLogger,
    private val message: String,
    private val extractor: () -> T,
) {
    fun get(): T {
        return summary { null }
    }

    @Suppress("TooGenericExceptionCaught")
    fun summary(extractor: (T.() -> String?)): T {
        try {
            kLogger.info { message }
            val start = System.nanoTime()
            val result = this.extractor()
            val end = System.nanoTime()

            val elapsed = Duration.ofNanos(end - start)
            kLogger.info { listOfNotNull(message, extractor.invoke(result), "at ${elapsed.format()}").joinToString(" ") }
            return result
        } catch (e: Exception) {
            kLogger.warn(e) { "$message: failed at ${e.message}" }
            throw e
        }
    }
}
