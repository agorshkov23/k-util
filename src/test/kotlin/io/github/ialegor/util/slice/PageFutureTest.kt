package io.github.ialegor.util.slice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class PageFutureTest {

    private val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_eachItem(size: Int) {
        val actual = mutableListOf<Int>()
        val future = list.toPageFuture(size)

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
        val future = list.toPageFuture(size)

        future.eachItem { item ->
            actual += item
            stop()
        }

        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_eachPage(size: Int) {
        val actual = mutableListOf<Int>()
        val future = list.toPageFuture(size)

        future.eachPage {
            actual += it.items
        }

        assertEquals(list, actual)
    }

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_eachPage_stop(size: Int) {
        val expected = list.take(size)
        val actual = mutableListOf<Int>()
        val future = list.toPageFuture(size)

        future.eachPage {
            actual += it.items
            stop()
        }

        assertEquals(expected, actual)
    }

    @Test
    fun test_empty() {
        val expected = emptyList<String>()
        val actual = PageFuture.empty<String>().toList()
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_filter(size: Int) {
        val predicate: (Int) -> Boolean = { it <= 5 }
        val expected = list.filter(predicate)

        val actual = list.toPageFuture(size)
            .filter(predicate)
            .toList()

        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_map(size: Int) {
        val expected = list.map { it * it }
        val actual = list.toPageFuture(size)
            .map { it * it }
            .toList()

        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_toList(size: Int) {
        val future = list.toPageFuture(size)
        val actual = future.toList()

        assertEquals(list, actual)
    }

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_toSequence(size: Int) {
        val future = list.toPageFuture(size)
        val actual = future.toSequence()

        assertEquals(list, actual.toList())
    }

    companion object {
        @JvmStatic
        fun provideSizes() = 1..15
    }
}
