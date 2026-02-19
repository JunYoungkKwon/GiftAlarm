package com.example.gifticonalarm.domain.usecase

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * 화면 표시에 사용할 날짜 문자열을 포맷한다.
 */
class FormatGifticonDateUseCase @Inject constructor() {

    operator fun invoke(date: Date, pattern: String = "yyyy.MM.dd"): String {
        return SimpleDateFormat(pattern, Locale.KOREAN).format(date)
    }
}
