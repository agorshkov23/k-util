package io.github.ialegor.util.slice

class PageResponse<T>(
    val page: Int,
    override val size: Int,
    override val items: List<T>,
) : SliceResponse<T> {
    constructor(request: PageRequest, items: List<T>) : this(request.page, request.size, items)

}
