package ml.ialegor.util.collection

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class FuturePageTest {

    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)

    @Test
    fun test_eachItem() {
        val actual = mutableListOf<Int>()
        val future = list.toFuturePage(5)

        future.eachItem { item ->
            actual += item
        }

        assertEquals(list, actual)
    }

    @Test
    fun test_eachPage() {
        val actual = mutableListOf<Int>()
        val future = list.toFuturePage(5)

        future.eachPage {
            actual += it.items
        }

        assertEquals(list, actual)
    }


    @Test
    fun test_toList() {
        val future = list.toFuturePage(5)
        val actual = future.toList()

        assertEquals(list, actual)
    }
}
