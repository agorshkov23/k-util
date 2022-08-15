package ml.ialegor.util.collection

import kotlin.math.max
import kotlin.math.min

class PageRequest(
    val page: Int,
    val size: Int,
) {
    fun next(): PageRequest {
        return PageRequest(page + 1, size)
    }

    override fun toString(): String = "page $page (size $size)"
}

class PageResponse<T>(
    val page: Int,
    val size: Int,
    val items: List<T>,
) {
    constructor(request: PageRequest, items: List<T>) : this(request.page, request.size, items)
}

class FuturePage<T>(
    size: Int,
    val options: Options = Options(),
    val extractor: PageRequest.() -> PageResponse<T>,
) {

    val size = max(1, min(size, options.maxSize))

    fun eachPage(handler: PageResponse<T>.() -> Unit) {
        var currentPage = PageRequest(options.initialPage, size)
        var currentPageResponse: PageResponse<T>
        do {
            currentPageResponse = extractor(currentPage)
            handler(currentPageResponse)
            currentPage = currentPage.next()
        } while (currentPageResponse.size > 0)
    }

    fun eachItem(handler: (T) -> Unit) {
        eachPage {
            for (item in items) {
                handler(item)
            }
        }
    }

    fun getPage(page: Int, size: Int = this.size): PageResponse<T> {

    }

    fun toList(): List<T> {
        val result = mutableListOf<T>()
        eachPage {
            result += items
        }
        return result
    }

    data class Options(
        val initialPage: Int = 0,
        val defaultSize: Int = 5,
        val maxSize: Int = 10,
    )
}

