package io.github.ialegor.util.collection

fun <T> List<T>.toFuturePage(size: Int): FuturePage<T> {
    return FuturePage(size, options = FuturePage.Options(0, maxSize = Int.MAX_VALUE)) { request ->
        val fromIndex = request.page * request.size
        if (this.size <= fromIndex) {
            return@FuturePage PageResponse(request, emptyList())
        }
        val toIndex = minOf(this.size, fromIndex + size)

        return@FuturePage PageResponse(request, subList(fromIndex, toIndex))
    }
}
