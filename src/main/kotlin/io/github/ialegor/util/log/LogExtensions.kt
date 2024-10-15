package io.github.ialegor.util.log

import mu.KLogger

fun Any.kLogger(): KLogger = kLogger(this::class)
