package io.github.ialegor.util.slice

interface SliceFuture<TItem> {
    val size: Int

    fun eachItem(handler: FutureManager.(TItem) -> Unit)

    fun eachSlice(handler: FutureManager.(SliceResponse<TItem>) -> Unit)

    fun filter(predicate: (TItem) -> Boolean): SliceFuture<TItem>

    fun <R> map(transform: (TItem) -> R): SliceFuture<R>

    fun toList(): List<TItem> {
        val result = mutableListOf<TItem>()
        eachItem { item ->
            result += item
        }
        return result
    }
}
