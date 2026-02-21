package com.example.gifticonalarm.ui.feature.shared.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

/**
 * 단발성 Effect를 처리한 뒤 consume 콜백을 실행한다.
 */
@Composable
fun <T> HandleRouteEffect(
    effect: T?,
    onConsumed: () -> Unit,
    onEffect: (T) -> Unit
) {
    LaunchedEffect(effect) {
        effect?.let {
            onEffect(it)
            onConsumed()
        }
    }
}

/**
 * 토스트 메시지를 일정 시간 후 자동으로 숨긴다.
 */
@Composable
fun AutoDismissToast(
    message: String?,
    onDismiss: () -> Unit,
    durationMillis: Long = 1900L
) {
    LaunchedEffect(message) {
        if (message == null) return@LaunchedEffect
        delay(durationMillis)
        onDismiss()
    }
}

