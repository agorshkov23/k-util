package io.github.ialegor.util.log

import mu.KLogger

class KLoggerMeasure(
    private val kLogger: KLogger,
    private val message: String,
) {
    fun <T> extract(extractor: () -> T): KLoggerExtractor<T> {
        return KLoggerExtractor(kLogger, message, extractor)
    }
}
