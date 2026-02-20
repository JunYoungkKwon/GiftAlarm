package com.example.gifticonalarm.domain.repository

import com.example.gifticonalarm.domain.model.NotificationSettings
import kotlinx.coroutines.flow.Flow

/**
 * 알림 설정 저장소 인터페이스.
 */
interface NotificationSettingsRepository {
    fun observeNotificationSettings(): Flow<NotificationSettings>
    suspend fun getNotificationSettings(): NotificationSettings
    suspend fun updateNotificationSettings(settings: NotificationSettings)
}
