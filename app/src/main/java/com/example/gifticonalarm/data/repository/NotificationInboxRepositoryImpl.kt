package com.example.gifticonalarm.data.repository

import com.example.gifticonalarm.data.local.preferences.NotificationInboxPreferencesDataSource
import com.example.gifticonalarm.domain.model.NotificationInboxState
import com.example.gifticonalarm.domain.repository.NotificationInboxRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 알림함 읽음/삭제 상태 저장소 구현체.
 */
class NotificationInboxRepositoryImpl @Inject constructor(
    private val dataSource: NotificationInboxPreferencesDataSource
) : NotificationInboxRepository {
    override fun observeInboxState(): Flow<NotificationInboxState> {
        return dataSource.observeInboxState()
    }

    override suspend fun markAsRead(notificationIds: Set<Long>) {
        dataSource.markAsRead(notificationIds)
    }

    override suspend fun deleteNotifications(notificationIds: Set<Long>) {
        dataSource.deleteNotifications(notificationIds)
    }
}
