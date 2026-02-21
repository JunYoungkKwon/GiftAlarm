package com.example.gifticonalarm.domain.repository

import com.example.gifticonalarm.domain.model.NotificationSettings

/**
 * 만료 알림 스케줄 동기화 인터페이스.
 */
interface NotificationScheduler {
    /**
     * 알림 설정에 맞춰 스케줄을 동기화한다.
     */
    suspend fun sync(settings: NotificationSettings)
}
