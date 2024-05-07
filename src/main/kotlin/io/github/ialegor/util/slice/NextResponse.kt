package io.github.ialegor.util.slice

open class NextResponse<TItem, TNext>(
    val next: TNext?,
    override val size: Int,
    override val items: List<TItem>,
    val request: NextRequest<TNext>,
) : SliceResponse<TItem>
