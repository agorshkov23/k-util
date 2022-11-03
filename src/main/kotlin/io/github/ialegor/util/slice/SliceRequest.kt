package io.github.ialegor.util.slice

import kotlin.jvm.Throws

interface SliceRequest<SELF : SliceRequest<SELF>> {

    val size: Int

    fun next(): SELF

    @Throws(IndexOutOfBoundsException::class)
    fun prev(): SELF

    fun prevOrNull(): SELF?
}
