package io.github.ialegor.util.slice

import kotlin.math.max
import kotlin.math.min

class BatchFuture<T>(
    override val size: Int,
    protected val extractManager: ExtractManager,
    val extractor: (BatchRequest) -> BatchResponse<T>,
) : SliceFuture<T> {

    constructor(currentSize: Int, maxSize: Int, extractor: (BatchRequest) -> BatchResponse<T>)
        : this(max(1, min(currentSize, maxSize)), ExtractManager(), extractor)

    protected constructor(size: Int, extractor: (BatchRequest) -> BatchResponse<T>)
        : this(size, size, extractor)

    override fun eachItem(handler: FutureManager.(T) -> Unit) {
        val manager = FutureManager()
        eachBatch(manager) { response ->
            for (item in response.items) {
                handler(manager, item)
                if (manager.stop) {
                    break
                }
            }
        }
    }

    fun eachBatch(handler: FutureManager.(BatchResponse<T>) -> Unit) {
        val manager = FutureManager()
        eachBatch(manager, handler)
    }

    private fun eachBatch(manager: FutureManager, handler: FutureManager.(BatchResponse<T>) -> Unit) {
        var currentBatchRequest = BatchRequest(0, size)
        var currentBatchResponse: BatchResponse<T>
        do {
            currentBatchResponse = extractor(currentBatchRequest)
            handler(manager, currentBatchResponse)
            if (manager.stop) {
                break
            }
            currentBatchRequest = currentBatchRequest.next()
        } while (currentBatchResponse.items.isNotEmpty() || extractManager.resume)
    }

    override fun filter(predicate: (T) -> Boolean): BatchFuture<T> {
        val extractManager = ExtractManager()
        return BatchFuture(size, extractManager) { request ->
            val response = extractor.invoke(request)
            extractManager.resume(response.items.isNotEmpty())
            val filtered = response.items.filter(predicate)
            BatchResponse(request, filtered)
        }
    }

    fun getBatch(offset: Int, size: Int = this.size): BatchResponse<T> {
        return getBatch(BatchRequest(offset, size))
    }

    private fun getBatch(request: BatchRequest): BatchResponse<T> {
        return extractor(request)
    }

    override fun <R> map(transform: (T) -> R): BatchFuture<R> {
        return BatchFuture(size) { request ->
            val response = extractor.invoke(request)
            val mapped = response.items.map(transform)
            BatchResponse(request, mapped)
        }
    }

    override fun toList(): List<T> {
        val result = mutableListOf<T>()
        eachBatch { response ->
            result += response.items
        }
        return result
    }

    override fun toSequence(): Sequence<T> {
        return sequence {
            var currentBatchRequest = BatchRequest(0, size)
            var currentBatchResponse: BatchResponse<T>
            do {
                currentBatchResponse = extractor(currentBatchRequest)
                yieldAll(currentBatchResponse.items)
                currentBatchRequest = currentBatchRequest.next()
            } while (currentBatchResponse.items.isNotEmpty())
        }
    }

    companion object {
        fun <T> empty(): BatchFuture<T> {
            return BatchFuture(1, 1) { request ->
                BatchResponse(request, emptyList())
            }
        }
    }
}
