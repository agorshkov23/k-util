package io.github.ialegor.util.slice

fun <T> List<T>.toBatchFuture(size: Int): BatchFuture<T> {
    return BatchFuture(size, Int.MAX_VALUE) { request ->
        val fromIndex = request.offset
        if (this.size <= request.offset) {
            return@BatchFuture BatchResponse(request, emptyList())
        }
        val toIndex = minOf(this.size, request.offset + size)

        return@BatchFuture BatchResponse(request, subList(fromIndex, toIndex))
    }
}

fun <T> List<T>.toPageFuture(size: Int): PageFuture<T> {
    return PageFuture(size, Int.MAX_VALUE) { request ->
        val fromIndex = request.page * request.size
        if (this.size <= fromIndex) {
            return@PageFuture PageResponse(request, emptyList())
        }
        val toIndex = minOf(this.size, fromIndex + size)

        return@PageFuture PageResponse(request, subList(fromIndex, toIndex))
    }
}
