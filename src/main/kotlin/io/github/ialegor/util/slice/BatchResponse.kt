package io.github.ialegor.util.slice

open class BatchResponse<TItem>(
    offset: Int,
    override val size: Int,
    override val items: List<TItem>,
) : BatchRequest(offset, size), SliceResponse<TItem> {
    constructor(request: BatchRequest, items: List<TItem>) : this(request.offset, request.size, items)

    override fun toString(): String = "offset $offset (size $size): ${items.size} items"

    open class Total<TItem>(
        offset: Int,
        override val size: Int,
        override val items: List<TItem>,
        override val total: Long,
    ) : BatchResponse<TItem>(offset, size, items), SliceResponse.Total
}
