package com.example.gifticonalarm.ui.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * 설정 라우트 진입점.
 */
@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.observeAsState(SettingsUiState()).value
    SettingsScreen(
        uiState = uiState,
        onPushEnabledChange = viewModel::updatePushEnabled,
        onNotify30DaysChange = viewModel::updateNotify30Days,
        onNotify7DaysChange = viewModel::updateNotify7Days,
        onNotify3DaysChange = viewModel::updateNotify3Days,
        onNotify1DayChange = viewModel::updateNotify1Day,
        onNotificationTimeChange = viewModel::updateNotificationTime
    )
}
