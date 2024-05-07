package io.github.ialegor.util.slice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal abstract class AbstractSliceFutureTest<TFuture : SliceFuture<Int>, TRequest, TResponse : SliceResponse<Int>> {

    private val list = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

    abstract fun createEmptyFuture(): TFuture

    abstract fun createFutureFromList(items: List<Int>, size: Int): TFuture

    abstract fun TFuture.eachSlice(handler: FutureManager.(TResponse) -> Unit)

    @ParameterizedTest(name = "size: {0}")
    @MethodSource("provideSizes")
    fun test_eachItem(size: Int) {
        val actual = mutableListOf<Int>()
        val future = createFutureFromList(list, size)

        future.eachItem { item ->
            actual += item
        }

        assertEquals(list, actual)
    }

    @ParameterizedTest(name = "size: {0}")
    @MethodSource("provideSizes")
    fun test_eachItem_stop(size: Int) {
        val expected = list.take(1)
        val actual = mutableListOf<Int>()
        val future = createFutureFromList(list, size)

        future.eachItem { item ->
            actual += item
            stop()
        }

        assertEquals(expected, actual)
    }

    @ParameterizedTest(name = "size: {0}")
    @MethodSource("provideSizes")
    fun test_eachSlice(size: Int) {
        val actual = mutableListOf<Int>()
        val future = createFutureFromList(list, size)

        future.eachSlice {
            actual += it.items
        }

        assertEquals(list, actual)
    }

    @ParameterizedTest(name = "size: {0}")
    @MethodSource("provideSizes")
    fun test_eachSlice_stop(size: Int) {
        val expected = list.take(size)
        val actual = mutableListOf<Int>()
        val future = createFutureFromList(list, size)

        future.eachSlice {
            actual += it.items
            stop()
        }

        assertEquals(expected, actual)
    }

    @Test
    @DisplayName("empty")
    fun test_empty() {
        val expected = emptyList<String>()
        val actual = createEmptyFuture().toList()
        assertEquals(expected, actual)
    }

    @DisplayName("filter")
    @ParameterizedTest(name = "size: {0}")
    @MethodSource("provideSizes")
    fun test_filter(size: Int) {
        val predicate: (Int) -> Boolean = { it <= 5 }
        val expected = list.filter(predicate)

        val actual = createFutureFromList(list, size)
            .filter(predicate)
            .toList()

        assertEquals(expected, actual)
    }

    @ParameterizedTest(name = "size: {0}")
    @MethodSource("provideSizes")
    fun test_map(size: Int) {
        val expected = list.map { it * it }
        val actual = createFutureFromList(list, size)
            .map { it * it }
            .toList()

        assertEquals(expected, actual)
    }

    @ParameterizedTest(name = "size: {0}")
    @MethodSource("provideSizes")
    fun test_toList(size: Int) {
        val future = createFutureFromList(list, size)
        val actual = future.toList()

        assertEquals(list, actual)
    }

    companion object {

        @JvmStatic
        fun provideSizes() = 1..15
    }
}
