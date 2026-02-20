package com.example.gifticonalarm.domain.model

/**
 * 만료 알림 설정 도메인 모델.
 */
data class NotificationSettings(
    val pushEnabled: Boolean = true,
    val selectedDays: Set<Int> = setOf(1, 3, 7),
    val notifyHour: Int = 9,
    val notifyMinute: Int = 0
)
