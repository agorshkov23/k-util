package io.github.ialegor.util.time

import java.time.Duration

fun Duration.format(): String {
    val parts = mutableListOf<String>()
    if (toDays() > 0) {
        parts += "${toDays()}d"
    }
    if (toHoursPart() > 0) {
        parts += "${toHoursPart()}h"
    }
    if (toMinutesPart() > 0) {
        parts += "${toMinutesPart()}m"
    }
    if (toSecondsPart() > 0) {
        parts += "${toSecondsPart()}s"
    }
    return parts.take(2).joinToString(" ")
}
