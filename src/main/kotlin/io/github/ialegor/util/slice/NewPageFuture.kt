package io.github.ialegor.util.slice

//typealias PageNumber = Int
//
//class NewPageFuture<TItem> protected constructor(
//    size: Int,
//    val initialPage: Int,
//    extractManager: ExtractManager,
//    extractor: (Request) -> Response<TItem>,
//) : NextFuture<TItem, PageNumber>(size, extractManager, { extractor.invoke(it) }) {
//
//    class Request(
//        next: PageNumber,
//        size: Int,
//    ) : NextRequest<PageNumber>(next, size)
//
//    class Response<TItem>(
//        next: PageNumber,
//        size: Int,
//        items: List<TItem>,
//        request: Request,
//    ) : NextResponse<TItem, PageNumber>(next, size, items, request)
//}
