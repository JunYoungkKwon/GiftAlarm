package com.example.gifticonalarm.domain.repository

import com.example.gifticonalarm.domain.model.NotificationSettings
import kotlinx.coroutines.flow.Flow

/**
 * 알림 설정 저장소 인터페이스.
 */
interface NotificationSettingsRepository {
    /**
     * 알림 설정 변화를 관찰한다.
     */
    fun observeNotificationSettings(): Flow<NotificationSettings>

    /**
     * 현재 알림 설정을 즉시 조회한다.
     */
    suspend fun getNotificationSettings(): NotificationSettings

    /**
     * 알림 설정을 저장한다.
     */
    suspend fun updateNotificationSettings(settings: NotificationSettings)
}
