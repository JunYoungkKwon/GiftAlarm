package com.example.gifticonalarm.ui.feature.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.model.NotificationInboxState
import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.usecase.DeleteNotificationsUseCase
import com.example.gifticonalarm.domain.usecase.GetAllGifticonsUseCase
import com.example.gifticonalarm.domain.usecase.MarkNotificationsAsReadUseCase
import com.example.gifticonalarm.domain.usecase.ObserveNotificationSettingsUseCase
import com.example.gifticonalarm.domain.usecase.ObserveNotificationInboxStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val notificationUiAssembler: NotificationUiAssembler,
    private val markNotificationsAsReadUseCase: MarkNotificationsAsReadUseCase,
    private val deleteNotificationsUseCase: DeleteNotificationsUseCase
) : ViewModel() {

    private val gifticons: LiveData<List<Gifticon>> = getAllGifticonsUseCase().asLiveData()
    private val settings: LiveData<NotificationSettings> = observeNotificationSettingsUseCase().asLiveData()
    private val inboxState: LiveData<NotificationInboxState> =
        observeNotificationInboxStateUseCase().asLiveData()

    val uiState: LiveData<NotificationUiState> = MediatorLiveData<NotificationUiState>().apply {
        val update = { value = buildUiState() }
        addSource(gifticons) { update() }
        addSource(settings) { update() }
        addSource(inboxState) { update() }
    }

    /**
     * 현재 보이는 알림을 모두 읽음 처리한다.
     */
    fun markAllAsRead() {
        markNotificationsAsRead(currentNotificationIds())
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
        val ids = currentNotificationIds()
        viewModelScope.launch {
            deleteNotificationsUseCase(ids)
        }
    }

    private fun markNotificationsAsRead(notificationIds: Set<Long>) {
        viewModelScope.launch {
            markNotificationsAsReadUseCase(notificationIds)
        }
    }

    private fun currentNotificationIds(): Set<Long> {
        return uiState.value?.notifications.orEmpty().map { it.id }.toSet()
    }

    private fun buildUiState(): NotificationUiState {
        return NotificationUiState(
            notifications = notificationUiAssembler.build(
                gifticons = gifticons.value.orEmpty(),
                settings = settings.value ?: NotificationSettings(),
                inboxState = inboxState.value ?: NotificationInboxState()
            )
        )
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
