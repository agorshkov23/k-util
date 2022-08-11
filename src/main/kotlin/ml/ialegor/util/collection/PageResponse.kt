package ml.ialegor.util.collection

data class PageResponse<T>(
    val page: Int,
    val size: Int,
    val items: List<T>,
) {
    constructor(request: PageRequest, items: List<T>) : this(request.page, request.size, items)
}
