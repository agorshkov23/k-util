package io.github.ialegor.util.time

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration

internal class ExtensionsTest {

    @ParameterizedTest
    @MethodSource("provideDurationFormats")
    fun test_Duration_format(expected: String, duration: Duration) {
        val actual = duration.format()
        assertEquals(expected, actual)
    }

    companion object {
        private val year = Duration.ofDays(365)
        private val month = Duration.ofDays(30)
        private val week = Duration.ofDays(7)
        private val day = Duration.ofDays(1)
        private val hour = Duration.ofHours(1)
        private val minute = Duration.ofMinutes(1)
        private val second = Duration.ofSeconds(1)
        private val millis = Duration.ofMillis(1)

        @JvmStatic
        fun provideDurationFormats(): List<Arguments> {
            return listOf(
                //  positive
                Arguments.of("1y", year),
                Arguments.of("1mnt", month),
                Arguments.of("1d", day),
                Arguments.of("1w", week),
                Arguments.of("1h", hour),
                Arguments.of("1min", minute),
                Arguments.of("1s", second),
                Arguments.of("1ms", millis),

                //  negative
                Arguments.of("1y ago", -year),
                Arguments.of("1mnt ago", -month),
                Arguments.of("1w ago", -week),
                Arguments.of("1d ago", -day),
                Arguments.of("1h ago", -hour),
                Arguments.of("1min ago", -minute),
                Arguments.of("1s ago", -second),
                Arguments.of("1ms ago", -millis),
            )
        }
    }
}

