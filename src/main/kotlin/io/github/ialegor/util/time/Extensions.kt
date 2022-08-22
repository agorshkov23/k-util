package io.github.ialegor.util.time

import java.time.Duration

fun Duration.format(): String {
    if (isNegative) {
        return (-this).format() + " ago"
    }

    val parts = mutableListOf<String>()

    var residual = this
    val years = residual.toDays() / 365
    if (years > 0) {
        parts += "${years}y"
        residual -= Duration.ofDays(years * 365)
    }

    val months = residual.toDays() / 30
    if (months > 0) {
        parts += "${months}mnt"
        residual -= Duration.ofDays(months * 30)
    }

    val weeks = residual.toDays() / 7
    if (weeks > 0) {
        parts += "${weeks}w"
        residual -= Duration.ofDays(weeks * 7)
    }

    if (residual.toDaysPart() > 0) {
        parts += "${residual.toDaysPart()}d"
    }

    if (residual.toHoursPart() > 0) {
        parts += "${residual.toHoursPart()}h"
    }

    if (residual.toMinutesPart() > 0) {
        parts += "${residual.toMinutesPart()}min"
    }

    if (residual.toSecondsPart() > 0) {
        parts += "${residual.toSecondsPart()}s"
    }

    if (residual.toMillisPart() > 0) {
        parts += "${residual.toMillisPart()}ms"
    }

    return parts.take(2).joinToString(" ")
}

operator fun Duration.unaryMinus(): Duration {
    return Duration.ofNanos(-toNanos())
}
