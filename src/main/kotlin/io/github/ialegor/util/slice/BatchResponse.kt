package io.github.ialegor.util.slice

open class BatchResponse<T>(
    offset: Int,
    override val size: Int,
    override val items: List<T>,
) : BatchRequest(offset, size), SliceResponse<T> {
    constructor(request: BatchRequest, items: List<T>) : this(request.offset, request.size, items)

    override fun toString(): String = "offset $offset (size $size): ${items.size} items"

}
