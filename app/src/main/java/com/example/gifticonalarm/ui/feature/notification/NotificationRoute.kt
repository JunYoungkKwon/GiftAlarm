package com.example.gifticonalarm.ui.feature.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * 알림 내역 라우트 진입점.
 */
@Composable
fun NotificationRoute(
    onBackClick: () -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.observeAsState(NotificationUiState())

    DisposableEffect(Unit) {
        onDispose {
            viewModel.markCurrentNotificationsAsRead()
        }
    }

    NotificationScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onMarkAllReadClick = viewModel::markAllAsRead,
        onDeleteAllClick = viewModel::deleteAll
    )
}
