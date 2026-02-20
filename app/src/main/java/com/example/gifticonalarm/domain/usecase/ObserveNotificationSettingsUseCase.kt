package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.repository.NotificationSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 알림 설정을 관찰한다.
 */
class ObserveNotificationSettingsUseCase @Inject constructor(
    private val repository: NotificationSettingsRepository
) {
    operator fun invoke(): Flow<NotificationSettings> {
        return repository.observeNotificationSettings()
    }
}
