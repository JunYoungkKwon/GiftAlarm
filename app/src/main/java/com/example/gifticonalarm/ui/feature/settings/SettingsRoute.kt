package com.example.gifticonalarm.ui.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gifticonalarm.ui.feature.shared.components.BottomToastBanner
import com.example.gifticonalarm.ui.feature.shared.effect.AutoDismissToast
import com.example.gifticonalarm.ui.feature.shared.effect.HandleRouteEffect

/**
 * 설정 라우트 진입점.
 */
@Composable
fun SettingsRoute(
    onNavigateToNotificationTime: () -> Unit,
    externalTimeText: String?,
    externalTimeVersion: Int,
    externalToastMessage: String?,
    externalToastVersion: Int,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.observeAsState(SettingsUiState()).value
    val effect by viewModel.effect.observeAsState()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var immediateTimeText by remember { mutableStateOf<String?>(null) }

    HandleRouteEffect(
        effect = effect,
        onConsumed = viewModel::consumeEffect
    ) { currentEffect ->
        when (currentEffect) {
            is SettingsEffect.ShowMessage -> {
                toastMessage = currentEffect.message
            }
        }
    }

    AutoDismissToast(message = toastMessage, onDismiss = { toastMessage = null })

    LaunchedEffect(externalToastVersion) {
        if (externalToastMessage.isNullOrBlank()) return@LaunchedEffect
        toastMessage = externalToastMessage
    }

    LaunchedEffect(externalTimeVersion) {
        if (externalTimeText.isNullOrBlank()) return@LaunchedEffect
        immediateTimeText = externalTimeText
    }

    LaunchedEffect(uiState.notificationTimeText, immediateTimeText) {
        if (shouldClearImmediateTime(
                stateTimeText = uiState.notificationTimeText,
                immediateTimeText = immediateTimeText
            )
        ) {
            immediateTimeText = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SettingsScreen(
            uiState = uiState.copy(
                notificationTimeText = resolveDisplayedNotificationTime(
                    stateTimeText = uiState.notificationTimeText,
                    immediateTimeText = immediateTimeText
                )
            ),
            onPushEnabledChange = viewModel::updatePushEnabled,
            onNotify30DaysChange = viewModel::updateNotify30Days,
            onNotify7DaysChange = viewModel::updateNotify7Days,
            onNotify3DaysChange = viewModel::updateNotify3Days,
            onNotify1DayChange = viewModel::updateNotify1Day,
            onNotificationTimeClick = onNavigateToNotificationTime
        )
        BottomToastBanner(message = toastMessage)
    }
}

private fun shouldClearImmediateTime(
    stateTimeText: String,
    immediateTimeText: String?
): Boolean {
    if (immediateTimeText == null) return false
    return stateTimeText == immediateTimeText
}

private fun resolveDisplayedNotificationTime(
    stateTimeText: String,
    immediateTimeText: String?
): String {
    return immediateTimeText ?: stateTimeText
}
