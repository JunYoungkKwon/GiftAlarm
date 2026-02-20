package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.repository.NotificationInboxRepository
import javax.inject.Inject

/**
 * 알림을 삭제 처리한다.
 */
class DeleteNotificationsUseCase @Inject constructor(
    private val repository: NotificationInboxRepository
) {
    suspend operator fun invoke(notificationIds: Set<Long>) {
        if (notificationIds.isEmpty()) return
        repository.deleteNotifications(notificationIds)
    }
}
