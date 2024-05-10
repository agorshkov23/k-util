package io.github.ialegor.util.slice

open class PageResponse<TItem>(
    page: Int,
    override val size: Int,
    override val items: List<TItem>,
) : PageRequest(page, size), SliceResponse<TItem> {
    constructor(request: PageRequest, items: List<TItem>) : this(request.page, request.size, items)

    override fun toString(): String = "page $page (size $size): ${items.size} items"
}
