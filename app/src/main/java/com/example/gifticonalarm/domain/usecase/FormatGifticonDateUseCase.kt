package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.DateFormatPolicy
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * 화면 표시에 사용할 날짜 문자열을 포맷한다.
 */
class FormatGifticonDateUseCase @Inject constructor() {

    operator fun invoke(date: Date, pattern: String = DateFormatPolicy.YMD_DOT): String {
        return SimpleDateFormat(pattern, Locale.KOREAN).format(date)
    }
}
