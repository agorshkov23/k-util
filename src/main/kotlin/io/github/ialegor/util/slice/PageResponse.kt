package io.github.ialegor.util.slice

open class PageResponse<T>(
    page: Int,
    override val size: Int,
    override val items: List<T>,
) : PageRequest(page, size), SliceResponse<T> {
    constructor(request: PageRequest, items: List<T>) : this(request.page, request.size, items)

    override fun toString(): String = "page $page (size $size): ${items.size} items"
}
