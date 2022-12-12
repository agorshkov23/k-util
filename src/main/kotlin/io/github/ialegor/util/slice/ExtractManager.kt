package io.github.ialegor.util.slice

import java.util.concurrent.atomic.AtomicBoolean

class ExtractManager {

    private val internalResume = AtomicBoolean(false)

    val resume: Boolean
        get() = internalResume.get()

    @Synchronized
    fun resume(value: Boolean = true):Boolean {
        internalResume.set(value)
        return internalResume.get()
    }
}
