package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.repository.NotificationSettingsRepository
import javax.inject.Inject

/**
 * 알림 설정을 저장한다.
 */
class UpdateNotificationSettingsUseCase @Inject constructor(
    private val repository: NotificationSettingsRepository
) {
    suspend operator fun invoke(settings: NotificationSettings) {
        repository.updateNotificationSettings(settings)
    }
}
