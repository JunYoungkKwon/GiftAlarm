package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.NotificationIdPolicy
import com.example.gifticonalarm.domain.model.NotificationInboxState
import com.example.gifticonalarm.domain.model.NotificationPolicy
import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.repository.GifticonRepository
import com.example.gifticonalarm.domain.repository.NotificationInboxRepository
import com.example.gifticonalarm.domain.repository.NotificationSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * 읽지 않은 알림 존재 여부를 관찰한다.
 */
class ObserveHasUnreadNotificationsUseCase @Inject constructor(
    private val gifticonRepository: GifticonRepository,
    private val notificationSettingsRepository: NotificationSettingsRepository,
    private val notificationInboxRepository: NotificationInboxRepository,
    private val calculateDdayUseCase: CalculateDdayUseCase
) {
    operator fun invoke(): Flow<Boolean> {
        return combine(
            gifticonRepository.observeAllGifticons(),
            notificationSettingsRepository.observeNotificationSettings(),
            notificationInboxRepository.observeInboxState()
        ) { gifticons, settings, inboxState ->
            hasUnreadNotifications(gifticons, settings, inboxState)
        }
    }

    private fun hasUnreadNotifications(
        gifticons: List<Gifticon>,
        settings: NotificationSettings,
        inboxState: NotificationInboxState
    ): Boolean {
        val nowMillis = System.currentTimeMillis()
        val selectedDays = settings.normalizedSelectedDays()

        val expiringNotificationIds = if (settings.pushEnabled) {
            gifticons
                .asSequence()
                .filter { !it.isUsed }
                .mapNotNull { gifticon ->
                    val dday = calculateDdayUseCase(gifticon.expiryDate).toInt()
                    if (dday !in selectedDays) return@mapNotNull null
                    NotificationIdPolicy.expiringSoonId(gifticon.id)
                }
        } else {
            emptySequence()
        }

        val newCouponNotificationIds = gifticons
            .asSequence()
            .filter { !it.isUsed }
            .filter { nowMillis - it.lastModifiedAt.time <= NotificationPolicy.RECENT_THRESHOLD_MILLIS }
            .map { NotificationIdPolicy.newCouponId(it.id) }

        return (expiringNotificationIds + newCouponNotificationIds).any { notificationId ->
            notificationId !in inboxState.deletedNotificationIds &&
                notificationId !in inboxState.readNotificationIds
        }
    }
}
