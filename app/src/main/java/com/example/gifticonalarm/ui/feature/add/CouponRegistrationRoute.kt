package com.example.gifticonalarm.ui.feature.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 쿠폰 등록 라우트 진입점.
 */
@Composable
fun CouponRegistrationRoute(
    onNavigateBack: () -> Unit,
    onRegistrationCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    CouponRegistrationScreen(
        modifier = modifier,
        onCloseClick = onNavigateBack,
        onRegisterClick = onRegistrationCompleted
    )
}
