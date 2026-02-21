package com.example.gifticonalarm.ui.feature.notification

import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.NotificationIdPolicy
import com.example.gifticonalarm.domain.model.NotificationInboxState
import com.example.gifticonalarm.domain.model.NotificationPolicy
import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.usecase.CalculateDdayUseCase
import com.example.gifticonalarm.ui.feature.shared.text.NotificationText
import com.example.gifticonalarm.ui.feature.shared.text.TextFormatters
import javax.inject.Inject

/**
 * 알림 화면 목록 조립 전담 클래스.
 */
class NotificationUiAssembler @Inject constructor(
    private val calculateDdayUseCase: CalculateDdayUseCase
) {

    fun build(
        gifticons: List<Gifticon>,
        settings: NotificationSettings,
        inboxState: NotificationInboxState,
        nowMillis: Long = System.currentTimeMillis()
    ): List<NotificationItem> {
        if (gifticons.isEmpty()) return emptyList()

        val selectedDays = settings.normalizedSelectedDays()

        val newCouponNotifications = gifticons
            .filter { !it.isUsed }
            .filter { nowMillis - it.lastModifiedAt.time <= NotificationPolicy.RECENT_THRESHOLD_MILLIS }
            .sortedByDescending { it.lastModifiedAt.time }
            .map {
                val notificationId = NotificationIdPolicy.newCouponId(it.id)
                NotificationItem(
                    id = notificationId,
                    type = NotificationType.NEW_COUPON,
                    title = NotificationText.TITLE_NEW_COUPON,
                    timeText = formatRelativeTimeText(nowMillis - it.lastModifiedAt.time),
                    message = NotificationText.newCouponMessage(it.name),
                    isUnread = notificationId !in inboxState.readNotificationIds
                )
            }

        val expiringSoonNotifications = if (settings.pushEnabled) {
            gifticons
                .filter { !it.isUsed }
                .mapNotNull { gifticon ->
                    val dday = calculateDdayUseCase(gifticon.expiryDate).toInt()
                    if (dday !in NotificationPolicy.NOTIFICATION_DAY_RANGE || dday !in selectedDays) return@mapNotNull null
                    val notificationId = NotificationIdPolicy.expiringSoonId(gifticon.id)
                    dday to NotificationItem(
                        id = notificationId,
                        type = NotificationType.EXPIRING_SOON,
                        title = NotificationText.TITLE_EXPIRING_SOON,
                        timeText = TextFormatters.ddayLabel(dday),
                        message = NotificationText.expiringSoonMessage(gifticon.name, dday),
                        isUnread = notificationId !in inboxState.readNotificationIds
                    )
                }
                .sortedBy { it.first }
                .map { it.second }
        } else {
            emptyList()
        }

        return (expiringSoonNotifications + newCouponNotifications)
            .filter { it.id !in inboxState.deletedNotificationIds }
            .take(MAX_ITEMS)
    }

    private companion object {
        const val MAX_ITEMS = 30
    }
}
