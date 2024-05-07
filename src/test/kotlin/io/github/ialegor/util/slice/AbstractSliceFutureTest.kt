package io.github.ialegor.util.slice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal abstract class AbstractSliceFutureTest<TFuture : SliceFuture<Int>, TRequest, TResponse : SliceResponse<Int>> {

    private val list = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    private val squareList = list.map { it * it }

    abstract fun createEmptyFuture(): TFuture

    abstract fun createFutureFromList(items: List<Int>, size: Int): TFuture

    @DisplayName("each item")
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

    @DisplayName("each item with stop")
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

    @DisplayName("each item with map and stop")
    @ParameterizedTest(name = "size: {0}")
    @MethodSource("provideSizes")
    fun test_eachItemWithMap_stop(size: Int) {
        val expected = squareList.take(1)
        val actual = mutableListOf<Int>()
        val future = createFutureFromList(list, size)

        future.map { it * it }.eachItem { item ->
            actual += item
            stop()
        }

        assertEquals(expected, actual)
    }

    @DisplayName("each slice")
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

    @DisplayName("each slice with stop")
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

    @Disabled
    @DisplayName("each slice with filter and stop")
    @ParameterizedTest(name = "size: {0}")
    @MethodSource("provideSizes")
    fun test_eachSliceWithFilter_stop(size: Int) {
        val expected = list.filter { it in 4..7 }.take(size)
        val actual = mutableListOf<Int>()
        val future = createFutureFromList(list, size)

        future.filter { it in 4..7 }.eachSlice {
            actual += it.items
            stop()
        }

        assertEquals(expected, actual)
    }

    @DisplayName("each slice with map and stop")
    @ParameterizedTest(name = "size: {0}")
    @MethodSource("provideSizes")
    fun test_eachSliceWithMap_stop(size: Int) {
        val expected = squareList.take(size)
        val actual = mutableListOf<Int>()
        val future = createFutureFromList(list, size)

        future.map { it * it }.eachSlice {
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

    @DisplayName("map")
    @ParameterizedTest(name = "size: {0}")
    @MethodSource("provideSizes")
    fun test_map(size: Int) {
        val expected = squareList
        val actual = createFutureFromList(list, size)
            .map { it * it }
            .toList()

        assertEquals(expected, actual)
    }

    @DisplayName("to list")
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
