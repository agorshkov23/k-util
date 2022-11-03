package io.github.ialegor.util.slice

import kotlin.math.max
import kotlin.math.min

class BatchFuture<T>(
    size: Int,
    val options: Options = Options(),
    val extractor: (BatchRequest) -> BatchResponse<T>,
) : SliceFuture<T> {

    override val size = max(1, min(size, options.maxSize))

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
        } while (currentBatchResponse.items.isNotEmpty())
    }

    fun getBatch(offset: Int, size: Int = this.size): BatchResponse<T> {
        return getBatch(BatchRequest(offset, size))
    }

    private fun getBatch(request: BatchRequest): BatchResponse<T> {
        return extractor(request)
    }

    override fun toList(): List<T> {
        val result = mutableListOf<T>()
        eachBatch { response ->
            result += response.items
        }
        return result
    }

    data class Options(
        val defaultSize: Int = 5,
        val maxSize: Int = 10,
    )
}
