package io.github.ialegor.util.log

import mu.KLogger

fun KLogger.measure(message: String): KLoggerMeasure {
    return KLoggerMeasure(this, message)
}
