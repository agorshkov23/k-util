package io.github.ialegor.util.slice

interface SliceResponse<TItem> : Iterable<TItem> {
    val size: Int
    val items: List<TItem>

    override fun iterator(): Iterator<TItem> = items.iterator()

    interface Total {
        val total: Long
    }
}
