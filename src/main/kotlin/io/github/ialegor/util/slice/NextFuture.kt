package io.github.ialegor.util.slice

open class NextFuture<TItem, TNext>(
    override val size: Int,
    protected val extractManager: ExtractManager,
    val extractor: (NextRequest<TNext>) -> NextResponse<TItem, TNext>,
) : SliceFuture<TItem> {

    constructor(size: Int, extractor: (NextRequest<TNext>) -> NextResponse<TItem, TNext>) : this(size, ExtractManager(), extractor)

    override fun eachItem(handler: FutureManager.(TItem) -> Unit) {
        val manager = FutureManager()
        eachNext(manager) { response ->
            for (item in response.items) {
                handler(manager, item)
                if (manager.stop) {
                    break
                }
            }
        }
    }

    override fun filter(predicate: (TItem) -> Boolean): NextFuture<TItem, TNext> {
        val extractManager = ExtractManager()
        return NextFuture(size, extractManager) { request ->
            val response = extractor.invoke(request)
            extractManager.resume(response.items.isNotEmpty())
            val filtered = response.items.filter(predicate)
            NextResponse(response.next, size, filtered, request)
        }
    }

    override fun <TResult> map(transform: (TItem) -> TResult): NextFuture<TResult, TNext> {
        return NextFuture(size, ExtractManager()) { request ->
            val response = extractor.invoke(request)
            val mapped = response.items.map(transform)
            NextResponse(response.next, size, mapped, request)
        }
    }

    fun eachNext(handler: FutureManager.(NextResponse<TItem, TNext>) -> Unit) {
        val manager = FutureManager()
        eachNext(manager, handler)
    }

    private fun eachNext(manager: FutureManager, handler: FutureManager.(NextResponse<TItem, TNext>) -> Unit) {
        var currentRequest = NextRequest<TNext>(null, size)
        var currentResponse: NextResponse<TItem, TNext>

        do {
            currentResponse = extractor(currentRequest)

            handler(manager, currentResponse)

            if (manager.stop) {
                break
            }

            currentRequest = NextRequest(currentResponse.next, size)
        } while (currentResponse.next != null || extractManager.resume)
    }

    companion object {
        fun <TItem, TNext> empty(): NextFuture<TItem, TNext> {
            return NextFuture(1) { request ->
                NextResponse(null, 0, emptyList(), request)
            }
        }
    }
}
