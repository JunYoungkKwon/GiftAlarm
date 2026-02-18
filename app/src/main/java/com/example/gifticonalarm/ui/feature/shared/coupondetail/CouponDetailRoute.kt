package com.example.gifticonalarm.ui.feature.shared.coupondetail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 쿠폰 상세 라우트 진입점.
 */
@Composable
fun CouponDetailRoute(
    couponId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    CouponDetailScreen(
        couponId = couponId,
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}
