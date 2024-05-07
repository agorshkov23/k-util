package io.github.ialegor.util.slice

import org.junit.jupiter.api.DisplayName

@DisplayName("Testing PageFuture, PageRequest and PageResponse")
internal class PageFutureTest : AbstractSliceFutureTest<PageFuture<Int>, PageRequest, PageResponse<Int>>() {

    override fun createEmptyFuture(): PageFuture<Int> {
        return PageFuture.empty()
    }

    override fun createFutureFromList(items: List<Int>, size: Int): PageFuture<Int> {
        return items.toPageFuture(size)
    }

    override fun PageFuture<Int>.eachSlice(handler: FutureManager.(PageResponse<Int>) -> Unit) {
        return eachPage {
            handler.invoke(this, it)
        }
    }
}
