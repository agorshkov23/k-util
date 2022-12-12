package io.github.ialegor.util.slice

interface SliceFuture<T> {
    val size: Int

    fun eachItem(handler: FutureManager.(T) -> Unit)

    fun filter(predicate: (T) -> Boolean): SliceFuture<T>

    fun <R> map(transform: (T) -> R): SliceFuture<R>

    fun toList(): List<T>
}
