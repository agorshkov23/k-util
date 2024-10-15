package io.github.ialegor.util.slice.interfaces

interface ISliceFuture<
    TItem,
    TRequest : ISliceFuture.IRequest,
    TResponse : ISliceFuture.IResponse<TRequest>,
    > : Iterator<TItem> {

    interface IRequest {

        val size: Int
    }

    interface IResponse<TRequest : IRequest> {

        val request: TRequest
    }
}

interface INextTokenSliceFuture<
    TItem,
    TNextToken,
    TRequest : INextTokenSliceFuture.IRequest<TNextToken>,
    TResponse : INextTokenSliceFuture.IResponse<TRequest, TNextToken>,
    > : ISliceFuture<TItem, TRequest, TResponse> {

    interface IRequest<TNextToken> : ISliceFuture.IRequest {

    }

    interface IResponse<TRequest : IRequest<TNextToken>, TNextToken> : ISliceFuture.IResponse<TRequest> {

    }
}

typealias Offset = Int

interface IOffsetSliceFuture<
    TItem,
    TRequest : IOffsetSliceFuture.IRequest,
    TResponse : IOffsetSliceFuture.IResponse<TRequest>,
    > : INextTokenSliceFuture<TItem, Offset, TRequest, TResponse> {

    interface IRequest : INextTokenSliceFuture.IRequest<Offset> {
    }

    interface IResponse<TRequest : IRequest> : INextTokenSliceFuture.IResponse<TRequest, Offset>
}

typealias Page = Int

interface IPageSliceFuture<
    TItem,
    TRequest : IPageSliceFuture.IRequest,
    TResponse : IPageSliceFuture.IResponse<TRequest>,
    > : INextTokenSliceFuture<TItem, Page, TRequest, TResponse> {

    interface IRequest : INextTokenSliceFuture.IRequest<Page> {
    }

    interface IResponse<TRequest : IRequest> : INextTokenSliceFuture.IResponse<TRequest, Page>
}
