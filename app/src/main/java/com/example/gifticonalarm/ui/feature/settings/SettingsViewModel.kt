package com.example.gifticonalarm.ui.feature.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val notificationTimeText: String = "09:00"
)

/**
 * 설정 화면 상태를 관리하는 ViewModel.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableLiveData(SettingsUiState())
    val uiState: LiveData<SettingsUiState> = _uiState

    fun updatePushEnabled(enabled: Boolean) {
        updateState { copy(isPushEnabled = enabled) }
    }

    fun updateNotify30Days(enabled: Boolean) {
        updateState { copy(notify30DaysBefore = enabled) }
    }

    fun updateNotify7Days(enabled: Boolean) {
        updateState { copy(notify7DaysBefore = enabled) }
    }

    fun updateNotify3Days(enabled: Boolean) {
        updateState { copy(notify3DaysBefore = enabled) }
    }

    fun updateNotify1Day(enabled: Boolean) {
        updateState { copy(notify1DayBefore = enabled) }
    }

    private fun updateState(reducer: SettingsUiState.() -> SettingsUiState) {
        val current = _uiState.value ?: SettingsUiState()
        _uiState.value = current.reducer()
    }
}
