package io.github.ialegor.util.slice

class BatchResponse<T>(
    val offset: Int,
    override val size: Int,
    override val items: List<T>,
) : SliceResponse<T> {
    constructor(request: BatchRequest, items: List<T>) : this(request.offset, request.size, items)
}
