package ml.ialegor.util.collection

data class PageRequest(
    val page: Int,
    val size: Int,
) {
    fun next(): PageRequest {
        return PageRequest(page + 1, size)
    }

    override fun toString(): String = "page $page (size $size)"
}

