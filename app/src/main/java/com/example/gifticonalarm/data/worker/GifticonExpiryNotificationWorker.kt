package com.example.gifticonalarm.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gifticonalarm.data.notification.GifticonNotificationDispatcher
import com.example.gifticonalarm.domain.repository.GifticonRepository
import com.example.gifticonalarm.domain.repository.NotificationSettingsRepository
import com.example.gifticonalarm.domain.usecase.CalculateDdayUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * 만료 알림 대상 기프티콘을 조회해 로컬 푸시를 발송하는 Worker.
 */
class GifticonExpiryNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val entryPoint = EntryPointAccessors.fromApplication(
        applicationContext,
        GifticonExpiryNotificationWorkerEntryPoint::class.java
    )
    private val gifticonRepository: GifticonRepository = entryPoint.gifticonRepository()
    private val notificationSettingsRepository: NotificationSettingsRepository = entryPoint.notificationSettingsRepository()
    private val calculateDdayUseCase: CalculateDdayUseCase = entryPoint.calculateDdayUseCase()
    private val notificationDispatcher: GifticonNotificationDispatcher = entryPoint.notificationDispatcher()

    override suspend fun doWork(): Result {
        return runCatching {
            val settings = notificationSettingsRepository.getNotificationSettings()
            if (!settings.pushEnabled) return Result.success()

            val selectedDays = settings.selectedDays.filter { it in 1..30 }.toSet()
            if (selectedDays.isEmpty()) return Result.success()

            val expiringGifticons = gifticonRepository.getAllGifticons()
                .asSequence()
                .filter { !it.isUsed }
                .filter { gifticon ->
                    calculateDdayUseCase(gifticon.expiryDate).toInt() in selectedDays
                }
                .toList()

            notificationDispatcher.notifyExpiringGifticons(expiringGifticons)
            Result.success()
        }.getOrElse {
            Result.retry()
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface GifticonExpiryNotificationWorkerEntryPoint {
    fun gifticonRepository(): GifticonRepository
    fun notificationSettingsRepository(): NotificationSettingsRepository
    fun calculateDdayUseCase(): CalculateDdayUseCase
    fun notificationDispatcher(): GifticonNotificationDispatcher
}
