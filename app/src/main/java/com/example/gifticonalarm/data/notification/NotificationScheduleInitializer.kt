package com.example.gifticonalarm.data.notification

import com.example.gifticonalarm.domain.repository.NotificationSettingsRepository
import com.example.gifticonalarm.domain.usecase.SyncNotificationScheduleUseCase
import javax.inject.Inject

/**
 * 앱 시작 시 저장된 알림 설정으로 스케줄을 재동기화한다.
 */
class NotificationScheduleInitializer @Inject constructor(
    private val notificationSettingsRepository: NotificationSettingsRepository,
    private val syncNotificationScheduleUseCase: SyncNotificationScheduleUseCase
) {
    suspend fun initialize() {
        val settings = notificationSettingsRepository.getNotificationSettings()
        syncNotificationScheduleUseCase(settings)
    }
}
