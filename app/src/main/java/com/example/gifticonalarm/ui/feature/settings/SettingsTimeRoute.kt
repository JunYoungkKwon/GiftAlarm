package com.example.gifticonalarm.ui.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
 * 알림 수신 시간 설정 라우트.
 */
@Composable
fun SettingsTimeRoute(
    onNavigateBack: () -> Unit,
    onSaveCompleted: (String) -> Unit,
    viewModel: SettingsTimeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.observeAsState(SettingsTimeUiState())
    val effect by viewModel.effect.observeAsState()
    var toastMessage by remember { mutableStateOf<String?>(null) }

    HandleRouteEffect(
        effect = effect,
        onConsumed = viewModel::consumeEffect
    ) { currentEffect ->
        when (currentEffect) {
            is SettingsTimeEffect.NavigateBack -> {
                onSaveCompleted(currentEffect.savedTimeText)
            }
            is SettingsTimeEffect.ShowMessage -> {
                toastMessage = currentEffect.message
            }
        }
    }

    AutoDismissToast(message = toastMessage, onDismiss = { toastMessage = null })

    Box(modifier = Modifier.fillMaxSize()) {
        SettingsTimeScreen(
            uiState = uiState,
            onBackClick = onNavigateBack,
            onSaveClick = viewModel::saveNotificationTime
        )
        BottomToastBanner(message = toastMessage)
    }
}
