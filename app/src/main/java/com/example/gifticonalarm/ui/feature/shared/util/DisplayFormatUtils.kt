package com.example.gifticonalarm.ui.feature.shared.util

import java.util.Locale

/**
 * 금액을 천 단위 구분 기호가 포함된 문자열로 포맷한다.
 */
fun formatAmountWithComma(amount: Int): String {
    return String.format(Locale.getDefault(), "%,d", amount)
}

/**
 * 연/월/일 값을 yyyy.MM.dd 형식으로 포맷한다.
 */
fun formatDateYmdDot(year: Int, month: Int, day: Int): String {
    return "%04d.%02d.%02d".format(year, month, day)
}

/**
 * 연/월/일 값을 yyyy-MM-dd 형식으로 포맷한다.
 */
fun formatDateYmdDash(year: Int, month: Int, day: Int): String {
    return "%04d-%02d-%02d".format(year, month, day)
}

