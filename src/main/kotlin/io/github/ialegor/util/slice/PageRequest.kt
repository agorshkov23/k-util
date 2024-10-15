package io.github.ialegor.util.slice

open class PageRequest(
    val page: Int,
    override val size: Int,
) : SliceRequest<PageRequest> {
    override fun next(): PageRequest {
        return PageRequest(page + 1, size)
    }

    override fun prev(): PageRequest {
        return prevOrNull() ?: throw IndexOutOfBoundsException("Page $page is less than 0")
    }

    override fun prevOrNull(): PageRequest? {
        if (page == 0) {
            return null
        }
        return PageRequest(page - 1, size)
    }

    override fun toString(): String = "page $page (size $size)"
}
