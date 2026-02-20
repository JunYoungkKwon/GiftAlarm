package com.example.gifticonalarm.ui.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gifticonalarm.ui.common.components.ToastBanner
import kotlinx.coroutines.delay

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

    LaunchedEffect(effect) {
        when (val currentEffect = effect) {
            is SettingsTimeEffect.NavigateBack -> {
                onSaveCompleted(currentEffect.savedTimeText)
                viewModel.consumeEffect()
            }
            is SettingsTimeEffect.ShowMessage -> {
                toastMessage = currentEffect.message
                viewModel.consumeEffect()
            }
            null -> Unit
        }
    }

    LaunchedEffect(toastMessage) {
        if (toastMessage == null) return@LaunchedEffect
        delay(1900L)
        toastMessage = null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SettingsTimeScreen(
            uiState = uiState,
            onBackClick = onNavigateBack,
            onSaveClick = viewModel::saveNotificationTime
        )
        toastMessage?.let { message ->
            ToastBanner(
                message = message,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            )
        }
    }
}
