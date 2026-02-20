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
    val minuteStep: Int = 0
)

sealed interface SettingsTimeEffect {
    data object NavigateBack : SettingsTimeEffect
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

    fun saveNotificationTime(periodIndex: Int, hour12: Int, minuteStep: Int) {
        val selectedHour12 = hour12.coerceIn(1, 12)
        val selectedMinute = (minuteStep * 5).coerceIn(0, 55)
        val hour24 = toHour24(periodIndex = periodIndex, hour12 = selectedHour12)

        viewModelScope.launch {
            runCatching {
                val current = getNotificationSettingsUseCase()
                val updated = current.copy(
                    notifyHour = hour24,
                    notifyMinute = selectedMinute
                )
                updateNotificationSettingsUseCase(updated)
                syncNotificationScheduleUseCase(updated)
            }.onSuccess {
                _effect.value = SettingsTimeEffect.NavigateBack
            }.onFailure {
                _effect.value = SettingsTimeEffect.ShowMessage("시간 저장에 실패했어요.")
            }
        }
    }

    fun consumeEffect() {
        _effect.value = null
    }

    private fun Flow<NotificationSettings>.mapToTimeUiState(): Flow<SettingsTimeUiState> {
        return map { settings ->
            val periodIndex = if (settings.notifyHour >= 12) 1 else 0
            val hour12 = toHour12(settings.notifyHour)
            val minuteStep = ((settings.notifyMinute.coerceIn(0, 59) + 2) / 5).coerceIn(0, 11)
            SettingsTimeUiState(
                periodIndex = periodIndex,
                hour12 = hour12,
                minuteStep = minuteStep
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
}
