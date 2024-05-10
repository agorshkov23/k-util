package io.github.ialegor.util.slice

import kotlin.jvm.Throws

interface SliceRequest<SELF : SliceRequest<SELF>> {

    val size: Int

    interface NextSlice<SELF : NextSlice<SELF>> : SliceRequest<SELF> {

        fun next(): SELF
    }

    interface PrevSlice<SELF : PrevSlice<SELF>> : SliceRequest<SELF> {

        @Throws(IndexOutOfBoundsException::class, UnsupportedOperationException::class)
        fun prev(): SELF

        fun prevOrNull(): SELF?
    }
}
