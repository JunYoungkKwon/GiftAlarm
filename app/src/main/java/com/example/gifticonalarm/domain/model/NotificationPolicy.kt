package com.example.gifticonalarm.domain.model

/**
 * 알림 관련 공통 정책값을 정의한다.
 */
object NotificationPolicy {
    val NOTIFICATION_DAY_RANGE: IntRange = 1..30
    const val RECENT_THRESHOLD_MILLIS: Long = 48L * 60L * 60L * 1000L
}

