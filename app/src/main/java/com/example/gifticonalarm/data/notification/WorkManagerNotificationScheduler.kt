package com.example.gifticonalarm.data.notification

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.gifticonalarm.data.worker.GifticonExpiryNotificationWorker
import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.repository.NotificationScheduler
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * WorkManager 기반 알림 스케줄러 구현체.
 */
class WorkManagerNotificationScheduler @Inject constructor(
    private val workManager: WorkManager
) : NotificationScheduler {

    override suspend fun sync(settings: NotificationSettings) {
        if (!settings.pushEnabled || settings.selectedDays.isEmpty()) {
            workManager.cancelUniqueWork(WORK_NAME)
            return
        }

        val request = PeriodicWorkRequestBuilder<GifticonExpiryNotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calculateInitialDelayMillis(settings.notifyHour, settings.notifyMinute), TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    private fun calculateInitialDelayMillis(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour.coerceIn(0, 23))
            set(Calendar.MINUTE, minute.coerceIn(0, 59))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
        }
        return (next.timeInMillis - now.timeInMillis).coerceAtLeast(0L)
    }

    private companion object {
        const val WORK_NAME = "gifticon_expiry_notification_work"
    }
}
