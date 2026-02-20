package com.example.gifticonalarm.ui.feature.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.usecase.GetNotificationSettingsUseCase
import com.example.gifticonalarm.domain.usecase.ObserveNotificationSettingsUseCase
import com.example.gifticonalarm.domain.usecase.SyncNotificationScheduleUseCase
import com.example.gifticonalarm.domain.usecase.UpdateNotificationSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 알림 설정 화면 UI 상태.
 */
data class SettingsUiState(
    val isPushEnabled: Boolean = true,
    val notify30DaysBefore: Boolean = false,
    val notify7DaysBefore: Boolean = true,
    val notify3DaysBefore: Boolean = true,
    val notify1DayBefore: Boolean = true,
    val notificationHour: Int = 9,
    val notificationMinute: Int = 0,
    val notificationTimeText: String = "09:00"
)

/**
 * 설정 화면 상태를 관리하는 ViewModel.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeNotificationSettingsUseCase: ObserveNotificationSettingsUseCase,
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val updateNotificationSettingsUseCase: UpdateNotificationSettingsUseCase,
    private val syncNotificationScheduleUseCase: SyncNotificationScheduleUseCase
) : ViewModel() {

    val uiState: LiveData<SettingsUiState> = observeNotificationSettingsUseCase()
        .mapToUiState()
        .asLiveData()

    fun updatePushEnabled(enabled: Boolean) {
        updateSettings { copy(pushEnabled = enabled) }
    }

    fun updateNotify30Days(enabled: Boolean) {
        updateSettings {
            copy(
                selectedDays = selectedDays.toMutableSet().apply {
                    if (enabled) add(30) else remove(30)
                }
            )
        }
    }

    fun updateNotify7Days(enabled: Boolean) {
        updateSettings {
            copy(
                selectedDays = selectedDays.toMutableSet().apply {
                    if (enabled) add(7) else remove(7)
                }
            )
        }
    }

    fun updateNotify3Days(enabled: Boolean) {
        updateSettings {
            copy(
                selectedDays = selectedDays.toMutableSet().apply {
                    if (enabled) add(3) else remove(3)
                }
            )
        }
    }

    fun updateNotify1Day(enabled: Boolean) {
        updateSettings {
            copy(
                selectedDays = selectedDays.toMutableSet().apply {
                    if (enabled) add(1) else remove(1)
                }
            )
        }
    }

    fun updateNotificationTime(hour: Int, minute: Int) {
        updateSettings {
            copy(
                notifyHour = hour.coerceIn(0, 23),
                notifyMinute = minute.coerceIn(0, 59)
            )
        }
    }

    private fun Flow<NotificationSettings>.mapToUiState(): Flow<SettingsUiState> {
        return map { settings ->
            SettingsUiState(
                isPushEnabled = settings.pushEnabled,
                notify30DaysBefore = 30 in settings.selectedDays,
                notify7DaysBefore = 7 in settings.selectedDays,
                notify3DaysBefore = 3 in settings.selectedDays,
                notify1DayBefore = 1 in settings.selectedDays,
                notificationHour = settings.notifyHour,
                notificationMinute = settings.notifyMinute,
                notificationTimeText = "%02d:%02d".format(settings.notifyHour, settings.notifyMinute)
            )
        }
    }

    private fun updateSettings(reducer: NotificationSettings.() -> NotificationSettings) {
        viewModelScope.launch {
            val current = getNotificationSettingsUseCase()
            val updated = current.reducer()
            updateNotificationSettingsUseCase(updated)
            syncNotificationScheduleUseCase(updated)
        }
    }
}
