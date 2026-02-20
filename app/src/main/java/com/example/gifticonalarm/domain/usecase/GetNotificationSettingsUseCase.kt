package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.repository.NotificationSettingsRepository
import javax.inject.Inject

/**
 * 현재 알림 설정을 조회한다.
 */
class GetNotificationSettingsUseCase @Inject constructor(
    private val repository: NotificationSettingsRepository
) {
    suspend operator fun invoke(): NotificationSettings {
        return repository.getNotificationSettings()
    }
}
