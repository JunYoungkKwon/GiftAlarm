package com.example.gifticonalarm.domain.repository

import com.example.gifticonalarm.domain.model.NotificationInboxState
import kotlinx.coroutines.flow.Flow

/**
 * 알림함 읽음/삭제 상태 저장소 인터페이스.
 */
interface NotificationInboxRepository {
    /**
     * 알림함 읽음/삭제 상태 변화를 관찰한다.
     */
    fun observeInboxState(): Flow<NotificationInboxState>

    /**
     * 전달받은 알림 ID들을 읽음 처리한다.
     */
    suspend fun markAsRead(notificationIds: Set<Long>)

    /**
     * 전달받은 알림 ID들을 삭제 처리한다.
     */
    suspend fun deleteNotifications(notificationIds: Set<Long>)
}
