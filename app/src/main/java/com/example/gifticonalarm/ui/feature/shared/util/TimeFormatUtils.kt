package com.example.gifticonalarm.ui.feature.shared.util

/**
 * 시/분 값을 HH:mm 문자열로 포맷한다.
 */
fun formatHourMinute(hour: Int, minute: Int): String {
    return "%02d:%02d".format(hour.coerceIn(0, 23), minute.coerceIn(0, 59))
}

/**
 * 숫자를 두 자리 문자열로 포맷한다.
 */
fun formatTwoDigits(value: Int): String {
    return "%02d".format(value.coerceAtLeast(0))
}
