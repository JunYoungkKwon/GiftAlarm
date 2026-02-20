package com.example.gifticonalarm.domain.repository

import com.example.gifticonalarm.domain.model.NotificationSettings

/**
 * 만료 알림 스케줄 동기화 인터페이스.
 */
interface NotificationScheduler {
    suspend fun sync(settings: NotificationSettings)
}
