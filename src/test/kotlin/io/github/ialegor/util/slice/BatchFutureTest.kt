package io.github.ialegor.util.slice

import org.junit.jupiter.api.DisplayName

@DisplayName("Testing BatchFuture, BatchRequest and BatchResponse")
internal class BatchFutureTest : AbstractISliceFutureTest<BatchFuture<Int>, BatchRequest, BatchResponse<Int>>() {

    override fun createEmptyFuture(): BatchFuture<Int> {
        return BatchFuture.empty()
    }

    override fun createFutureFromList(items: List<Int>, size: Int): BatchFuture<Int> {
        return items.toBatchFuture(size)
    }
}
