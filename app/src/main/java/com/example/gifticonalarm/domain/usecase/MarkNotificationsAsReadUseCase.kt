package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.repository.NotificationInboxRepository
import javax.inject.Inject

/**
 * 알림을 읽음 상태로 표시한다.
 */
class MarkNotificationsAsReadUseCase @Inject constructor(
    private val repository: NotificationInboxRepository
) {
    suspend operator fun invoke(notificationIds: Set<Long>) {
        if (notificationIds.isEmpty()) return
        repository.markAsRead(notificationIds)
    }
}
