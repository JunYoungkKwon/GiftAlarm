package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.repository.NotificationScheduler
import javax.inject.Inject

/**
 * 알림 설정 기준으로 스케줄을 동기화한다.
 */
class SyncNotificationScheduleUseCase @Inject constructor(
    private val scheduler: NotificationScheduler
) {
    suspend operator fun invoke(settings: NotificationSettings) {
        scheduler.sync(settings)
    }
}
