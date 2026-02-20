package com.example.gifticonalarm.ui.feature.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.NotificationInboxState
import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.usecase.CalculateDdayUseCase
import com.example.gifticonalarm.domain.usecase.DeleteNotificationsUseCase
import com.example.gifticonalarm.domain.usecase.GetAllGifticonsUseCase
import com.example.gifticonalarm.domain.usecase.MarkNotificationsAsReadUseCase
import com.example.gifticonalarm.domain.usecase.ObserveNotificationSettingsUseCase
import com.example.gifticonalarm.domain.usecase.ObserveNotificationInboxStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 알림 내역 화면 UI 상태를 관리한다.
 */
@HiltViewModel
class NotificationViewModel @Inject constructor(
    getAllGifticonsUseCase: GetAllGifticonsUseCase,
    observeNotificationSettingsUseCase: ObserveNotificationSettingsUseCase,
    observeNotificationInboxStateUseCase: ObserveNotificationInboxStateUseCase,
    private val calculateDdayUseCase: CalculateDdayUseCase,
    private val markNotificationsAsReadUseCase: MarkNotificationsAsReadUseCase,
    private val deleteNotificationsUseCase: DeleteNotificationsUseCase
) : ViewModel() {

    private val gifticons: LiveData<List<Gifticon>> = getAllGifticonsUseCase().asLiveData()
    private val settings: LiveData<NotificationSettings> = observeNotificationSettingsUseCase().asLiveData()
    private val inboxState: LiveData<NotificationInboxState> =
        observeNotificationInboxStateUseCase().asLiveData()

    val uiState: LiveData<NotificationUiState> = MediatorLiveData<NotificationUiState>().apply {
        val update = {
            value = NotificationUiState(
                notifications = buildNotifications(
                    gifticons = gifticons.value.orEmpty(),
                    settings = settings.value ?: NotificationSettings(),
                    inboxState = inboxState.value ?: NotificationInboxState()
                )
            )
        }
        addSource(gifticons) { update() }
        addSource(settings) { update() }
        addSource(inboxState) { update() }
    }

    private fun buildNotifications(
        gifticons: List<Gifticon>,
        settings: NotificationSettings,
        inboxState: NotificationInboxState
    ): List<NotificationItem> {
        if (gifticons.isEmpty()) return emptyList()

        val nowMillis = System.currentTimeMillis()
        val selectedDays = settings.selectedDays.filter { it in 1..30 }.toSet()

        val newCouponNotifications = gifticons
            .filter { !it.isUsed }
            .filter { nowMillis - it.lastModifiedAt.time <= RECENT_THRESHOLD_MILLIS }
            .sortedByDescending { it.lastModifiedAt.time }
            .map {
                    NotificationItem(
                        id = 2_000_000L + it.id,
                        type = NotificationType.NEW_COUPON,
                        title = "신규 등록",
                        timeText = relativeTimeText(nowMillis - it.lastModifiedAt.time),
                        message = "${it.name} 쿠폰이 등록되었습니다.",
                        isUnread = (2_000_000L + it.id) !in inboxState.readNotificationIds
                    )
                }

        val expiringSoonNotifications = if (settings.pushEnabled) {
            gifticons
                .filter { !it.isUsed }
                .mapNotNull { gifticon ->
                    val dday = calculateDdayUseCase(gifticon.expiryDate).toInt()
                    if (dday !in selectedDays) return@mapNotNull null
                    NotificationItem(
                        id = 1_000_000L + gifticon.id,
                        type = NotificationType.EXPIRING_SOON,
                        title = "만료 임박",
                        timeText = "D-$dday",
                        message = "${gifticon.name} 쿠폰 유효기간이 ${dday}일 남았습니다.",
                        isUnread = (1_000_000L + gifticon.id) !in inboxState.readNotificationIds
                    )
                }
                .sortedBy { it.timeText.removePrefix("D-").toIntOrNull() ?: Int.MAX_VALUE }
        } else {
            emptyList()
        }

        return (expiringSoonNotifications + newCouponNotifications)
            .filter { it.id !in inboxState.deletedNotificationIds }
            .take(MAX_ITEMS)
    }

    /**
     * 현재 보이는 알림을 모두 읽음 처리한다.
     */
    fun markAllAsRead() {
        markNotificationsAsRead(uiState.value?.notifications.orEmpty().map { it.id }.toSet())
    }

    /**
     * 화면 진입 후 사용자가 확인한 알림을 읽음 처리한다.
     */
    fun markCurrentNotificationsAsRead() {
        markAllAsRead()
    }

    /**
     * 현재 보이는 알림을 전체 삭제한다.
     */
    fun deleteAll() {
        val ids = uiState.value?.notifications.orEmpty().map { it.id }.toSet()
        viewModelScope.launch {
            deleteNotificationsUseCase(ids)
        }
    }

    private fun markNotificationsAsRead(notificationIds: Set<Long>) {
        viewModelScope.launch {
            markNotificationsAsReadUseCase(notificationIds)
        }
    }

    private fun relativeTimeText(diffMillis: Long): String {
        if (diffMillis <= 0L) return "방금 전"
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
        val days = TimeUnit.MILLISECONDS.toDays(diffMillis)
        return when {
            minutes < 1L -> "방금 전"
            minutes < 60L -> "${minutes}분 전"
            hours < 24L -> "${hours}시간 전"
            days < 2L -> "어제"
            else -> "${days}일 전"
        }
    }

    private companion object {
        const val MAX_ITEMS = 30
        const val RECENT_THRESHOLD_MILLIS = 48L * 60L * 60L * 1000L
    }
}

/**
 * 알림 화면 UI 상태.
 */
data class NotificationUiState(
    val notifications: List<NotificationItem> = emptyList()
)

/**
 * 알림 행 모델.
 */
data class NotificationItem(
    val id: Long,
    val type: NotificationType,
    val title: String,
    val timeText: String,
    val message: String,
    val isUnread: Boolean = false
)

/**
 * 알림 유형.
 */
enum class NotificationType {
    EXPIRING_SOON,
    NEW_COUPON,
    INFO
}
