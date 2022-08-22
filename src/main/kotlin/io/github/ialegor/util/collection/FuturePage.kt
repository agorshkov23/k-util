package io.github.ialegor.util.collection

import kotlin.math.max
import kotlin.math.min

class FuturePage<T>(
    size: Int,
    val options: Options = Options(),
    val extractor: (PageRequest) -> PageResponse<T>,
) {

    val size = max(1, min(size, options.maxSize))

    fun eachItem(handler: FutureManager.(T) -> Unit) {
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
        var currentPageRequest = PageRequest(options.initialPage, size)
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

    fun toList(): List<T> {
        val result = mutableListOf<T>()
        eachPage { response ->
            result += response.items
        }
        return result
    }

    data class Options(
        val initialPage: Int = 0,
        val defaultSize: Int = 5,
        val maxSize: Int = 10,
    )
}
