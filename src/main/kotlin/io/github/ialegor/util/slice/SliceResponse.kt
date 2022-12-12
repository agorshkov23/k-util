package io.github.ialegor.util.slice

interface SliceResponse<T> : Iterable<T> {
    val size: Int
    val items: List<T>

    override fun iterator(): Iterator<T> = items.iterator()
}
