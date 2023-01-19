/**
 * Deprecated since 0.7.0.
 */
package io.github.ialegor.util.logging

import io.github.ialegor.util.log.KLoggerMeasure
import io.github.ialegor.util.log.kLogger
import io.github.ialegor.util.log.measure
import mu.KLogger
import kotlin.reflect.KClass

/**
 * Deprecated since 0.7.0.
 */
@Deprecated("Use kLogger", ReplaceWith("kLogger()", "io.github.ialegor.util.log.kLogger"), level = DeprecationLevel.WARNING)
fun Any.logger(): KLogger = kLogger()

/**
 * Deprecated since 0.7.0.
 */
@Deprecated("Use kLogger", ReplaceWith("kLogger(kClass)", "io.github.ialegor.util.log.kLogger"), level = DeprecationLevel.WARNING)
fun Any.logger(kClass: KClass<*>): KLogger = kLogger(kClass)

/**
 * Deprecated since 0.7.0.
 */
@Deprecated("Use measure extension from package io.github.ialegor.util.log", ReplaceWith("measure(message)", "io.github.ialegor.util.log.measure"), level = DeprecationLevel.WARNING)
fun KLogger.measure(message: String): KLoggerMeasure = measure(message)
