package io.github.ialegor.util.slice

open class NextRequest<TNext>(
    val next: TNext?,
    override val size: Int,
) : SliceRequest<NextRequest<TNext>>
