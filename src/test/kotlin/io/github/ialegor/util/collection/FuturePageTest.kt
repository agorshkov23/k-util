package io.github.ialegor.util.collection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class FuturePageTest {

    private val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_eachItem(size: Int) {
        val actual = mutableListOf<Int>()
        val future = list.toFuturePage(size)

        future.eachItem { item ->
            actual += item
        }

        assertEquals(list, actual)
    }

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_eachPage(size: Int) {
        val actual = mutableListOf<Int>()
        val future = list.toFuturePage(size)

        future.eachPage {
            actual += it.items
        }

        assertEquals(list, actual)
    }

    @ParameterizedTest
    @MethodSource("provideSizes")
    fun test_toList(size: Int) {
        val future = list.toFuturePage(size)
        val actual = future.toList()

        assertEquals(list, actual)
    }

    companion object {
        @JvmStatic
        fun provideSizes() = 1..15
    }
}
