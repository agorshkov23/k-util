package io.github.ialegor.util.slice

import java.util.concurrent.atomic.AtomicBoolean

class FutureManager {

    private val internalStop = AtomicBoolean(false)

    val stop: Boolean
        get() = internalStop.get()

    @Synchronized
    fun stop(value: Boolean = true): Boolean {
        internalStop.set(value)
        return internalStop.get()
    }
}
