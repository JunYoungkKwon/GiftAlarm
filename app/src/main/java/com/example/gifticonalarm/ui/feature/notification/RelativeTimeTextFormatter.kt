package com.example.gifticonalarm.ui.feature.notification

import com.example.gifticonalarm.ui.feature.shared.text.NotificationText
import java.util.concurrent.TimeUnit

fun formatRelativeTimeText(diffMillis: Long): String {
    if (diffMillis <= 0L) return NotificationText.JUST_NOW
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffMillis)
    return when {
        minutes < 1L -> NotificationText.JUST_NOW
        minutes < 60L -> NotificationText.minutesAgo(minutes)
        hours < 24L -> NotificationText.hoursAgo(hours)
        days < 2L -> NotificationText.YESTERDAY
        else -> NotificationText.daysAgo(days)
    }
}
