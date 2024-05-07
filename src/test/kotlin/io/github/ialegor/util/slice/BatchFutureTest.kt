package io.github.ialegor.util.slice

import org.junit.jupiter.api.DisplayName

@DisplayName("Testing BatchFuture, BatchRequest and BatchResponse")
internal class BatchFutureTest : AbstractSliceFutureTest<BatchFuture<Int>, BatchRequest, BatchResponse<Int>>() {

    override fun createEmptyFuture(): BatchFuture<Int> {
        return BatchFuture.empty()
    }

    override fun createFutureFromList(items: List<Int>, size: Int): BatchFuture<Int> {
        return items.toBatchFuture(size)
    }

    override fun BatchFuture<Int>.eachSlice(handler: FutureManager.(BatchResponse<Int>) -> Unit) {
        return eachBatch {
            handler.invoke(this, it)
        }
    }
}
