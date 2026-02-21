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
 * 알림 수신 시간 설정 화면 상태.
 */
data class SettingsTimeUiState(
    val periodIndex: Int = 0,
    val hour12: Int = 9,
    val minute: Int = 0
)

sealed interface SettingsTimeEffect {
    data class NavigateBack(val savedTimeText: String) : SettingsTimeEffect
    data class ShowMessage(val message: String) : SettingsTimeEffect
}

/**
 * 알림 수신 시간 설정 화면 ViewModel.
 */
@HiltViewModel
class SettingsTimeViewModel @Inject constructor(
    observeNotificationSettingsUseCase: ObserveNotificationSettingsUseCase,
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val updateNotificationSettingsUseCase: UpdateNotificationSettingsUseCase,
    private val syncNotificationScheduleUseCase: SyncNotificationScheduleUseCase
) : ViewModel() {

    private val _effect = MutableLiveData<SettingsTimeEffect?>()
    val effect: LiveData<SettingsTimeEffect?> = _effect

    val uiState: LiveData<SettingsTimeUiState> = observeNotificationSettingsUseCase()
        .mapToTimeUiState()
        .asLiveData()

    fun saveNotificationTime(periodIndex: Int, hour12: Int, minute: Int) {
        val selectedHour12 = hour12.coerceIn(1, 12)
        val selectedMinute = minute.coerceIn(0, 59)
        val hour24 = toHour24(periodIndex = periodIndex, hour12 = selectedHour12)

        viewModelScope.launch {
            runCatching { persistNotificationTime(hour24, selectedMinute) }
                .onSuccess {
                _effect.emitEffect(
                    SettingsTimeEffect.NavigateBack(
                        savedTimeText = formatHourMinute(hour24, selectedMinute)
                    )
                )
            }.onFailure {
                _effect.emitEffect(SettingsTimeEffect.ShowMessage(CommonText.MESSAGE_TIME_SAVE_FAILED))
            }
        }
    }

    fun consumeEffect() {
        _effect.consumeEffect()
    }

    private fun Flow<NotificationSettings>.mapToTimeUiState(): Flow<SettingsTimeUiState> {
        return map { settings ->
            SettingsTimeUiState(
                periodIndex = toPeriodIndex(settings.notifyHour),
                hour12 = toHour12(settings.notifyHour),
                minute = normalizeMinute(settings.notifyMinute)
            )
        }
    }

    private fun toHour12(hour24: Int): Int {
        val normalized = hour24.coerceIn(0, 23)
        return when {
            normalized == 0 -> 12
            normalized > 12 -> normalized - 12
            else -> normalized
        }
    }

    private fun toHour24(periodIndex: Int, hour12: Int): Int {
        val isPm = periodIndex == 1
        return when {
            isPm && hour12 == 12 -> 12
            isPm -> hour12 + 12
            hour12 == 12 -> 0
            else -> hour12
        }
    }

    private fun toPeriodIndex(hour24: Int): Int {
        return if (hour24 >= 12) 1 else 0
    }

    private fun normalizeMinute(minute: Int): Int {
        return minute.coerceIn(0, 59)
    }

    private fun NotificationSettings.withNotificationTime(
        notifyHour: Int,
        notifyMinute: Int
    ): NotificationSettings {
        return copy(
            notifyHour = notifyHour,
            notifyMinute = notifyMinute
        )
    }

    private suspend fun persistNotificationTime(hour24: Int, minute: Int) {
        val current = getNotificationSettingsUseCase()
        val updated = current.withNotificationTime(
            notifyHour = hour24,
            notifyMinute = minute
        )
        updateNotificationSettingsUseCase(updated)
        syncNotificationScheduleUseCase(updated)
    }
}
