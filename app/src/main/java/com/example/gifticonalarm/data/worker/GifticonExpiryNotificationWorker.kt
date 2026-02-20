package com.example.gifticonalarm.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gifticonalarm.data.notification.GifticonNotificationDispatcher
import com.example.gifticonalarm.domain.repository.GifticonRepository
import com.example.gifticonalarm.domain.repository.NotificationSettingsRepository
import com.example.gifticonalarm.domain.usecase.CalculateDdayUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * 만료 알림 대상 기프티콘을 조회해 로컬 푸시를 발송하는 Worker.
 */
@HiltWorker
class GifticonExpiryNotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val gifticonRepository: GifticonRepository,
    private val notificationSettingsRepository: NotificationSettingsRepository,
    private val calculateDdayUseCase: CalculateDdayUseCase,
    private val notificationDispatcher: GifticonNotificationDispatcher
) : CoroutineWorker(appContext, workerParams) {

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
