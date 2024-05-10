package io.github.ialegor.util.slice

import kotlin.math.max
import kotlin.math.min

open class PageFuture<TItem> protected constructor(
    override val size: Int,
    val initialPage: Int,
    protected val extractManager: ExtractManager,
    val extractor: (PageRequest) -> PageResponse<TItem>,
) : SliceFuture<TItem> {

    constructor(currentSize: Int, maxSize: Int, initialPage: Int, extractor: (PageRequest) -> PageResponse<TItem>)
        : this(max(1, min(currentSize, maxSize)), initialPage, ExtractManager(), extractor)

    constructor(currentSize: Int, maxSize: Int, extractor: (PageRequest) -> PageResponse<TItem>)
        : this(currentSize, maxSize, 0, extractor)

    protected constructor(size: Int, extractor: (PageRequest) -> PageResponse<TItem>)
        : this(size, size, 0, extractor)

    override fun eachItem(handler: FutureManager.(TItem) -> Unit) {
        val manager = FutureManager()
        eachPage(manager) { response ->
            for (item in response.items) {
                handler(manager, item)
                if (manager.stop) {
                    break
                }
            }
        }
    }

    override fun eachSlice(handler: FutureManager.(SliceResponse<TItem>) -> Unit) {
        eachPage(handler)
    }

    fun eachPage(handler: FutureManager.(PageResponse<TItem>) -> Unit) {
        val manager = FutureManager()
        eachPage(manager, handler)
    }

    private fun eachPage(manager: FutureManager, handler: FutureManager.(PageResponse<TItem>) -> Unit) {
        var currentPageRequest = PageRequest(initialPage, size)
        var currentPageResponse: PageResponse<TItem>
        do {
            currentPageResponse = extractor(currentPageRequest)
            handler(manager, currentPageResponse)
            if (manager.stop) {
                break
            }
            currentPageRequest = currentPageRequest.next()
        } while (currentPageResponse.items.isNotEmpty() || extractManager.resume)
    }

    override fun filter(predicate: (TItem) -> Boolean): PageFuture<TItem> {
        val extractManager = ExtractManager()
        return PageFuture(size, initialPage, extractManager) { request ->
            val response = extractor.invoke(request)
            extractManager.resume(response.items.isNotEmpty())
            val filtered = response.items.filter(predicate)
            PageResponse(request, filtered)
        }
    }

    fun getPage(page: Int, size: Int = this.size): PageResponse<TItem> {
        return getPage(PageRequest(page, size))
    }

    fun getPage(request: PageRequest): PageResponse<TItem> {
        return extractor(request)
    }

    override fun <R> map(transform: (TItem) -> R): PageFuture<R> {
        return PageFuture(size) { request ->
            val response = extractor.invoke(request)
            val mapped = response.items.map(transform)
            PageResponse(request, mapped)
        }
    }

    override fun toList(): List<TItem> {
        val result = mutableListOf<TItem>()
        eachPage { response ->
            result += response.items
        }
        return result
    }

    companion object {
        fun <T> empty(): PageFuture<T> {
            return PageFuture(1, 1) { request ->
                PageResponse(request, emptyList())
            }
        }
    }
}
