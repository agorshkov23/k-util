package io.github.ialegor.util.slice

class BatchRequest(
    val offset: Int,
    override val size: Int,
) : SliceRequest<BatchRequest> {
    override fun next(): BatchRequest {
        return BatchRequest(offset + size, size)
    }

    override fun prev(): BatchRequest {
        return prevOrNull() ?: throw IndexOutOfBoundsException("Offset $offset is less than 0")
    }

    override fun prevOrNull(): BatchRequest? {
        val offset = offset - size
        if (offset < 0) {
            return null
        }
        return BatchRequest(offset, size)
    }

    override fun toString(): String = "offset $offset (size $size)"
}

