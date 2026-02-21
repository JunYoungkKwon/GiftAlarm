package com.example.gifticonalarm.ui.feature.shared.util

import com.example.gifticonalarm.ui.feature.add.bottomsheet.ExpirationDate

/**
 * 사용일 텍스트(`yyyy-MM-dd` 또는 `yyyy.MM.dd`)를 날짜 선택 모델로 변환한다.
 * 파싱에 실패하면 오늘 날짜를 반환한다.
 */
fun parseUsedAtTextOrToday(rawText: String): ExpirationDate {
    val normalized = rawText.trim().replace('.', '-')
    val tokens = normalized.split("-")
    if (tokens.size != 3) return ExpirationDate.today()

    val year = tokens[0].toIntOrNull() ?: return ExpirationDate.today()
    val month = tokens[1].toIntOrNull() ?: return ExpirationDate.today()
    val day = tokens[2].toIntOrNull() ?: return ExpirationDate.today()

    val boundedMonth = month.coerceIn(1, 12)
    return ExpirationDate(
        year = year,
        month = boundedMonth,
        day = day.coerceIn(1, ExpirationDate.maxDayOfMonth(year, boundedMonth))
    )
}

/**
 * 날짜 선택 모델을 사용일 텍스트(`yyyy-MM-dd`)로 변환한다.
 */
fun ExpirationDate.toUsedAtText(): String = formatDateYmdDash(year, month, day)
