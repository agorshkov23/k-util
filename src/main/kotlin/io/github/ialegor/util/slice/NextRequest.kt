package io.github.ialegor.util.slice

open class NextRequest<TNext>(
    val next: TNext?,
    override val size: Int,
) : SliceRequest<NextRequest<TNext>> {

    override fun next(): NextRequest<TNext> {
        throw UnsupportedOperationException("Method next() is not available in NextRequest!")
    }

    override fun prev(): NextRequest<TNext> {
        throw UnsupportedOperationException("Method prev() is not available in NextRequest!")
    }

    override fun prevOrNull(): NextRequest<TNext>? {
        return null
    }
}
