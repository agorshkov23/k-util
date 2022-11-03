package io.github.ialegor.util.slice

interface SliceResponse<T> {
    val size: Int
    val items: List<T>
}
