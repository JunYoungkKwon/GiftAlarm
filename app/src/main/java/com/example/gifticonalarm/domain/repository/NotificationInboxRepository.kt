package com.example.gifticonalarm.domain.repository

import com.example.gifticonalarm.domain.model.NotificationInboxState
import kotlinx.coroutines.flow.Flow

/**
 * 알림함 읽음/삭제 상태 저장소 인터페이스.
 */
interface NotificationInboxRepository {
    fun observeInboxState(): Flow<NotificationInboxState>
    suspend fun markAsRead(notificationIds: Set<Long>)
    suspend fun deleteNotifications(notificationIds: Set<Long>)
}
