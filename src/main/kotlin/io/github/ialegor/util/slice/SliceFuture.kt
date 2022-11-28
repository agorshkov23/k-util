package io.github.ialegor.util.slice

interface SliceFuture<T> {
    val size: Int

    fun eachItem(handler: FutureManager.(T) -> Unit)

    fun toList(): List<T>
}
