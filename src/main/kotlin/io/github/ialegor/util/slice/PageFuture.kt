package io.github.ialegor.util.slice

import kotlin.math.max
import kotlin.math.min

open class PageFuture<T> protected constructor(
    override val size: Int,
    val initialPage: Int,
    protected val extractManager: ExtractManager,
    val extractor: (PageRequest) -> PageResponse<T>,
) : SliceFuture<T> {

    constructor(currentSize: Int, maxSize: Int, initialPage: Int, extractor: (PageRequest) -> PageResponse<T>)
        : this(max(1, min(currentSize, maxSize)), initialPage, ExtractManager(), extractor)

    constructor(currentSize: Int, maxSize: Int, extractor: (PageRequest) -> PageResponse<T>)
        : this(currentSize, maxSize, 0, extractor)

    protected constructor(size: Int, extractor: (PageRequest) -> PageResponse<T>)
        : this(size, size, 0, extractor)

    override fun eachItem(handler: FutureManager.(T) -> Unit) {
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

    override fun eachSlice(handler: FutureManager.(SliceResponse<T>) -> Unit) {
        eachPage(handler)
    }

    fun eachPage(handler: FutureManager.(PageResponse<T>) -> Unit) {
        val manager = FutureManager()
        eachPage(manager, handler)
    }

    private fun eachPage(manager: FutureManager, handler: FutureManager.(PageResponse<T>) -> Unit) {
        var currentPageRequest = PageRequest(initialPage, size)
        var currentPageResponse: PageResponse<T>
        do {
            currentPageResponse = extractor(currentPageRequest)
            handler(manager, currentPageResponse)
            if (manager.stop) {
                break
            }
            currentPageRequest = currentPageRequest.next()
        } while (currentPageResponse.items.isNotEmpty() || extractManager.resume)
    }

    override fun filter(predicate: (T) -> Boolean): PageFuture<T> {
        val extractManager = ExtractManager()
        return PageFuture(size, initialPage, extractManager) { request ->
            val response = extractor.invoke(request)
            extractManager.resume(response.items.isNotEmpty())
            val filtered = response.items.filter(predicate)
            PageResponse(request, filtered)
        }
    }

    fun getPage(page: Int, size: Int = this.size): PageResponse<T> {
        return getPage(PageRequest(page, size))
    }

    fun getPage(request: PageRequest): PageResponse<T> {
        return extractor(request)
    }

    override fun <R> map(transform: (T) -> R): PageFuture<R> {
        return PageFuture(size) { request ->
            val response = extractor.invoke(request)
            val mapped = response.items.map(transform)
            PageResponse(request, mapped)
        }
    }

    override fun toList(): List<T> {
        val result = mutableListOf<T>()
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
