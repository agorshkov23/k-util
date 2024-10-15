package io.github.ialegor.util.log

import mu.KLogger
import mu.KotlinLogging
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

fun kLogger(name: String): KLogger = KotlinLogging.logger(name)

fun kLogger(underlyingLogger: Logger): KLogger = KotlinLogging.logger(underlyingLogger)

fun kLogger(clazz: Class<*>): KLogger = kLogger(LoggerFactory.getLogger(clazz))

fun kLogger(klass: KClass<*>): KLogger = kLogger(klass.java)
