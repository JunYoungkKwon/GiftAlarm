package com.example.gifticonalarm.domain.model

/**
 * 알림함 읽음/삭제 상태를 나타낸다.
 */
data class NotificationInboxState(
    val readNotificationIds: Set<Long> = emptySet(),
    val deletedNotificationIds: Set<Long> = emptySet()
)
