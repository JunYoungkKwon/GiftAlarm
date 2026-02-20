package com.example.gifticonalarm.data.repository

import com.example.gifticonalarm.data.local.preferences.NotificationSettingsPreferencesDataSource
import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.repository.NotificationSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 알림 설정 저장소 구현체.
 */
class NotificationSettingsRepositoryImpl @Inject constructor(
    private val dataSource: NotificationSettingsPreferencesDataSource
) : NotificationSettingsRepository {
    override fun observeNotificationSettings(): Flow<NotificationSettings> {
        return dataSource.observeNotificationSettings()
    }

    override suspend fun getNotificationSettings(): NotificationSettings {
        return dataSource.getNotificationSettings()
    }

    override suspend fun updateNotificationSettings(settings: NotificationSettings) {
        dataSource.updateNotificationSettings(settings)
    }
}
