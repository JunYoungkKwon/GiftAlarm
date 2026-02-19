package com.example.gifticonalarm.domain.usecase

import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * 기준 날짜 대비 D-day를 계산한다.
 */
class CalculateDdayUseCase @Inject constructor() {

    operator fun invoke(targetDate: Date): Long {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val target = Calendar.getInstance().apply {
            time = targetDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        return TimeUnit.MILLISECONDS.toDays(target - today)
    }
}
