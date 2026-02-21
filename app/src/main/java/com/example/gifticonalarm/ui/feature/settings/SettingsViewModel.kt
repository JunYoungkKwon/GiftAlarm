package com.example.gifticonalarm.ui.feature.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gifticonalarm.domain.model.NotificationSettings
import com.example.gifticonalarm.domain.usecase.GetNotificationSettingsUseCase
import com.example.gifticonalarm.domain.usecase.ObserveNotificationSettingsUseCase
import com.example.gifticonalarm.domain.usecase.SyncNotificationScheduleUseCase
import com.example.gifticonalarm.domain.usecase.UpdateNotificationSettingsUseCase
import com.example.gifticonalarm.ui.feature.shared.livedata.consumeEffect
import com.example.gifticonalarm.ui.feature.shared.livedata.emitEffect
import com.example.gifticonalarm.ui.feature.shared.text.CommonText
import com.example.gifticonalarm.ui.feature.shared.util.formatHourMinute
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
    val notificationTimeText: String = formatHourMinute(9, 0)
)

sealed interface SettingsEffect {
    data class ShowMessage(val message: String) : SettingsEffect
}

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
    private val _effect = MutableLiveData<SettingsEffect?>()
    val effect: LiveData<SettingsEffect?> = _effect

    val uiState: LiveData<SettingsUiState> = observeNotificationSettingsUseCase()
        .mapToUiState()
        .asLiveData()

    fun updatePushEnabled(enabled: Boolean) {
        updateSettings { copy(pushEnabled = enabled) }
        _effect.emitEffect(SettingsEffect.ShowMessage(
            if (enabled) CommonText.MESSAGE_PUSH_ENABLED else CommonText.MESSAGE_PUSH_DISABLED
        ))
    }

    fun updateNotify30Days(enabled: Boolean) {
        updateNotifyDay(day = 30, enabled = enabled)
    }

    fun updateNotify7Days(enabled: Boolean) {
        updateNotifyDay(day = 7, enabled = enabled)
    }

    fun updateNotify3Days(enabled: Boolean) {
        updateNotifyDay(day = 3, enabled = enabled)
    }

    fun updateNotify1Day(enabled: Boolean) {
        updateNotifyDay(day = 1, enabled = enabled)
    }

    private fun updateNotifyDay(day: Int, enabled: Boolean) {
        updateSettings {
            copy(
                selectedDays = selectedDays.updateDaySelection(day, enabled)
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
                notificationTimeText = formatHourMinute(settings.notifyHour, settings.notifyMinute)
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

    private fun Set<Int>.updateDaySelection(day: Int, enabled: Boolean): Set<Int> {
        return toMutableSet().apply {
            if (enabled) add(day) else remove(day)
        }
    }

    fun consumeEffect() {
        _effect.consumeEffect()
    }
}
