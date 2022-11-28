package io.github.ialegor.util.slice

import kotlin.math.max
import kotlin.math.min

open class PageFuture<T>(
    currentSize: Int,
    maxSize: Int,
    val initialPage: Int,
    val extractor: (PageRequest) -> PageResponse<T>
) : SliceFuture<T> {

    override val size = max(1, min(currentSize, maxSize))

    constructor(currentSize: Int, maxSize: Int, extractor: (PageRequest) -> PageResponse<T>) : this(currentSize, maxSize, 0, extractor)

    @Suppress("DEPRECATION")
    @Deprecated("Use another constructor")
    constructor(size: Int, options: Options = Options(), extractor: (PageRequest) -> PageResponse<T>) : this(size, options.maxSize, options.initialPage, extractor)

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
        } while (currentPageResponse.items.isNotEmpty())
    }

    fun getPage(page: Int, size: Int = this.size): PageResponse<T> {
        return getPage(PageRequest(page, size))
    }

    fun getPage(request: PageRequest): PageResponse<T> {
        return extractor(request)
    }

    companion object {
        fun <T> empty(): PageFuture<T> {
            return PageFuture(1, 1) { request ->
                PageResponse(request, emptyList())
            }
        }
    }

    override fun toList(): List<T> {
        val result = mutableListOf<T>()
        eachPage { response ->
            result += response.items
        }
        return result
    }

    @Deprecated("Do not use this class")
    data class Options(
        val initialPage: Int = 0,
        val defaultSize: Int = 5,
        val maxSize: Int = 10,
    )
}
