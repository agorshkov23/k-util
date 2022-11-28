package io.github.ialegor.util.slice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class BatchFutureTest {

    private val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_eachItem(size: Int) {
        val actual = mutableListOf<Int>()
        val future = list.toBatchFuture(size)

        future.eachItem { item ->
            actual += item
        }

        assertEquals(list, actual)
    }

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_eachItem_stop(size: Int) {
        val expected = list.take(1)
        val actual = mutableListOf<Int>()
        val future = list.toBatchFuture(size)

        future.eachItem { item ->
            actual += item
            stop()
        }

        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_eachBatch(size: Int) {
        val actual = mutableListOf<Int>()
        val future = list.toBatchFuture(size)

        future.eachBatch {
            actual += it.items
        }

        assertEquals(list, actual)
    }

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_eachBatch_stop(size: Int) {
        val expected = list.take(size)
        val actual = mutableListOf<Int>()
        val future = list.toBatchFuture(size)

        future.eachBatch {
            actual += it.items
            stop()
        }

        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_toList(size: Int) {
        val future = list.toBatchFuture(size)
        val actual = future.toList()

        assertEquals(list, actual)
    }

    companion object {
        @JvmStatic
        fun provideSizes() = 1..15
    }
}
