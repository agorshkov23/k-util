package io.github.ialegor.util.logging

import io.github.ialegor.util.time.format
import mu.KLogger
import mu.KotlinLogging
import java.time.Duration
import kotlin.reflect.KClass

fun Any.logger(): KLogger = logger(this::class)

fun Any.logger(kClass: KClass<*>): KLogger = KotlinLogging.logger(kClass.java.name)

fun KLogger.measure(message: String): KLoggerMeasure {
    return KLoggerMeasure(this, message)
}

class KLoggerMeasure(
    private val log: KLogger,
    private val message: String,
) {
    fun <T> extract(extractor: () -> T): KLoggerExtractor<T> {
        return KLoggerExtractor(log, message, extractor)
    }
}

class KLoggerExtractor<T>(
    private val log: KLogger,
    private val message: String,
    private val extractor: () -> T,
) {
    fun get(): T {
        return summary { null }
    }

    @Suppress("TooGenericExceptionCaught")
    fun summary(extractor: (T.() -> String?)): T {
        try {
            log.info { message }
            val start = System.nanoTime()
            val result = this.extractor()
            val end = System.nanoTime()

            val elapsed = Duration.ofNanos(end - start)
            log.info { listOfNotNull(message, extractor.invoke(result), "at ${elapsed.format()}").joinToString(" ") }
            return result
        } catch (e: Exception) {
            log.warn(e) { "$message: failed at ${e.message}" }
            throw e
        }
    }
}
